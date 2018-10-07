package com.example.anshulsharma.augmentedvideocalling;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity())
                .setActionBarTitle("  Profile");
        // Inflate the layout for this fragment

        View profileView=inflater.inflate(R.layout.fragment_profile,container,false);

        TextView name=profileView.findViewById(R.id.name);
        TextView email=profileView.findViewById(R.id.email);

        TextView posts=profileView.findViewById(R.id.posts);
        TextView contacts=profileView.findViewById(R.id.contacts);
        TextView meetings=profileView.findViewById(R.id.meetings);

        Button signOut=profileView.findViewById(R.id.sign_out);

        FloatingActionButton fab = profileView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ChatBot.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getActivity());
            }
        });


        name.setText(MainActivity.user.getDisplayName());
        email.setText(MainActivity.user.getEmail());


        try {
            posts.setText(String.valueOf(HomeFragment.listView.getAdapter().getCount()));
            contacts.setText(String.valueOf(ContactsFragment.listView.getAdapter().getCount()));
            meetings.setText(String.valueOf(NotificationFragment.listView.getAdapter().getCount()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return profileView;
    }

}
