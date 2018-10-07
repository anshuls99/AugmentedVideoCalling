package com.example.anshulsharma.augmentedvideocalling;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;



/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    public static ArrayList<Invites> invites=new ArrayList<>();

    public static NotificationAdapter adapter;

    public DatabaseReference mFirebaseDatabase;
    public Query notificationPost;

    public static ListView listView;

    SwipeRefreshLayout mySwipeRefreshLayout;




    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View notificationView=inflater.inflate(R.layout.contacts_listview,container,false);


        ((MainActivity) getActivity())
                .setActionBarTitle("  Notifications");

        mFirebaseDatabase=FirebaseDatabase.getInstance().getReference();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        listView= notificationView.findViewById(R.id.numView);

        adapter = new NotificationAdapter(getActivity(), invites);
        listView.setAdapter(adapter);

        View emptyView=notificationView.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mySwipeRefreshLayout=notificationView.findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        listView.setAdapter(adapter);
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mySwipeRefreshLayout.setRefreshing(false);
                            }
                        },1000);
                    }
                }
        );

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(invites.get(position).getmId().length()<32){
                    Toast.makeText(getActivity(), "Agora Id is Invalid", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getContext(), VideoChatViewActivity.class);
                    intent.putExtra("appId", invites.get(position).getmId());
                    startActivity(intent);
                }

                return true;
            }
        });

        for(int i=0;i<listView.getAdapter().getCount();i++){
            try {
                notificationPost = mFirebaseDatabase.child("Notification").orderByChild("mMessage").equalTo(invites.get(i).getmMessage());
                if(formatter.parse(invites.get(i).getmDate()).before(formatter.parse(formatter.format(date)))){
                    invites.remove(i);
                    adapter.notifyDataSetChanged();

                    try {

                        notificationPost.addListenerForSingleValueEvent(new ValueEventListener() {
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
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return notificationView;
    }

}
