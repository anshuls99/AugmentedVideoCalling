package com.example.anshulsharma.augmentedvideocalling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPost extends AppCompatActivity {

    Spinner mGenderSpinner;

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference userPosts;

    public String status;

    public EditText postText;

    SimpleDateFormat formatter;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        setTitle("Post");
        getSupportActionBar().setElevation(0);

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();


        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        mFirebaseDatabase=FirebaseDatabase.getInstance();

        userPosts=mFirebaseDatabase.getReference().child("Posts");



        postText=findViewById(R.id.edit_post);


        setupSpinner();
    }

    public void submitPost(View view){

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //create new user
                Contacts contacts=new Contacts(MainActivity.user.getDisplayName(),postText.getText().toString(),status,formatter.format(date));
                userPosts.push().setValue(contacts);
                //HomeFragment.homecontacts.add(contacts);
                //HomeFragment.homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userPosts.addListenerForSingleValueEvent(eventListener);
        Toast.makeText(this, "Posted!!!", Toast.LENGTH_SHORT).show();

        finish();

    }

    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);

        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        status="Public";
                    }else {
                        status="Private";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                status="public";
            }
        });
    }
}
