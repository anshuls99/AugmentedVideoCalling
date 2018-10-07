package com.example.anshulsharma.augmentedvideocalling;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<Contacts> {


    public ContactsAdapter(@NonNull Context context, ArrayList<Contacts> contacts) {
        super(context,0, contacts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView=convertView;

        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.fragment_contacts,parent,false);
        }

        final Contacts currentContact=getItem(position);

        TextView miwokTextView= (TextView) listItemView.findViewById(R.id.miwok);
        miwokTextView.setText(currentContact.getmName());

        TextView defaultTextView= (TextView) listItemView.findViewById(R.id.english);
        defaultTextView.setText(currentContact.getmEmail());


//        ImageView imageView= (ImageView) listItemView.findViewById(R.id.imageView3);
//        if(currentWord.getImageResourceId()==0){
//            imageView.setVisibility(View.GONE);
//        }else {
//            imageView.setImageResource(currentWord.getImageResourceId());
//        }


        return  listItemView;


    }

}
