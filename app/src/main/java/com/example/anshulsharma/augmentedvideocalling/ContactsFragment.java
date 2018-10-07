package com.example.anshulsharma.augmentedvideocalling;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

//import com.google.firebase.auth.ExportedUserRecord;
//import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.auth.ListUsersPage;
//import com.google.firebase.auth.ListUsersPage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends android.support.v4.app.Fragment {

    public static ArrayList<Contacts> contacts=new ArrayList<>();

    public static ContactsAdapter adapter;

    SwipeRefreshLayout mySwipeRefreshLayout;

    public String m_Text = "asda";

    public static ListView listView;


    public ContactsFragment() {
        // Required empty public constructor
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle("  Contacts");

    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity) getActivity())
                .setActionBarTitle("Contacts");


        View contactsView=inflater.inflate(R.layout.contacts_listview,container,false);


        listView= contactsView.findViewById(R.id.numView);

        adapter = new ContactsAdapter(getActivity(), contacts);
        listView.setAdapter(adapter);

        View emptyView=contactsView.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);


        mySwipeRefreshLayout=contactsView.findViewById(R.id.swiperefresh);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
                builder.setTitle("Enter Your Agora ID");


// Set up the input
                final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if(m_Text.length()<32){
                            Toast.makeText(getActivity(), "Enter the Correct Agora Id", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent = new Intent(getContext(), VideoChatViewActivity.class);
                            intent.putExtra("appId", m_Text);
                            startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SendInvites.class);
                intent.putExtra("email",contacts.get(position).getmEmail());
                startActivity(intent);
                return true;
            }
        });

//        FloatingActionButton fab = contactsView.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), SendInvites.class);
//                startActivity(intent);
//            }
//        });



        return contactsView;

    }

}
