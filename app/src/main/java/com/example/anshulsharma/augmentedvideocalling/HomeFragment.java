package com.example.anshulsharma.augmentedvideocalling;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{

    public static ArrayList<Contacts> homecontacts=new ArrayList<>();

    public static HomeAdapter homeAdapter;

    public DatabaseReference mFirebaseDatabase;
    public Query userPosts;
    public static ListView listView;

    SwipeRefreshLayout mySwipeRefreshLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle("  Home");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View postView=inflater.inflate(R.layout.home_listview,container,false);

        ((MainActivity) getActivity())
                .setActionBarTitle("Home");

        mFirebaseDatabase=FirebaseDatabase.getInstance().getReference();


        listView= (ListView) postView.findViewById(R.id.postsList);

        homeAdapter = new HomeAdapter(getActivity(), homecontacts);

        View emptyView=postView.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        //homeAdapter.notifyDataSetChanged();
        listView.setAdapter(homeAdapter);


        mySwipeRefreshLayout= postView.findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        listView.setAdapter(homeAdapter);
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mySwipeRefreshLayout.setRefreshing(false);
                            }
                        },1000);
                    }
                }
        );


        FloatingActionButton fab = postView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddPost.class);
                startActivity(intent);
//                Intent intent=new Intent(getContext(),ChatBot.class);
//                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                userPosts = mFirebaseDatabase.child("Posts").orderByChild("mPosts").equalTo(homecontacts.get(position).getmPosts());

                if(!homecontacts.get(position).getmName().equals(MainActivity.user.getDisplayName()));

                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
                    builder1.setIcon(android.R.drawable.ic_dialog_alert);
                    builder1.setTitle("Are You Sure?");
                    builder1.setMessage("Do you want to delete this Post?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    homecontacts.remove(position);
                                    homeAdapter.notifyDataSetChanged();

                                    try {

                                        userPosts.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    postSnapshot.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                //Log.e(TAG, "onCancelled", databaseError.toException());
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

                return true;
            }
        });

        return postView;
    }

}
