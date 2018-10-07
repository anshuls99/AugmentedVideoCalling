package com.example.anshulsharma.augmentedvideocalling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Invites> {


    public NotificationAdapter(@NonNull Context context, ArrayList<Invites> notification) {
        super(context,0,notification);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView=convertView;

        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.fragment_notification,parent,false);
        }

        final Invites currentInvites=getItem(position);

        TextView message= (TextView) listItemView.findViewById(R.id.message);
        message.setText(currentInvites.getmMessage());

        TextView name=listItemView.findViewById(R.id.name);
        name.setText("Invited By " + currentInvites.getmName());

        TextView date=listItemView.findViewById(R.id.date);
        date.setText(currentInvites.getmDate());


        return  listItemView;


    }
}
