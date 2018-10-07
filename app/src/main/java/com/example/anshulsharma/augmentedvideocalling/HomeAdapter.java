package com.example.anshulsharma.augmentedvideocalling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeAdapter extends ArrayAdapter<Contacts> {


    public HomeAdapter(@NonNull Context context, ArrayList<Contacts> contacts) {
        super(context,0, contacts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View homeView=convertView;


        if(homeView==null){
             homeView= LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,parent,false);
        }

        final Contacts currentContact=getItem(position);

        TextView posts= (TextView) homeView.findViewById(R.id.posted);
        TextView name=homeView.findViewById(R.id.name);
        TextView date=homeView.findViewById(R.id.date);

        CardView postCard=homeView.findViewById(R.id.post);

            postCard.setVisibility(View.VISIBLE);
            posts.setText(currentContact.getmPosts());
            name.setText("Posted By:- "+currentContact.getmName());
            date.setText(currentContact.getmDate());





//        ImageView imageView= (ImageView) listItemView.findViewById(R.id.imageView3);
//        if(currentWord.getImageResourceId()==0){
//            imageView.setVisibility(View.GONE);
//        }else {
//            imageView.setImageResource(currentWord.getImageResourceId());
//        }


        return  homeView;


    }

}
