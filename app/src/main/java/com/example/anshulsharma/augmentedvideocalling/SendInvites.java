package com.example.anshulsharma.augmentedvideocalling;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SendInvites extends AppCompatActivity {

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference sendInvite;

    EditText agoraId;
    EditText message;
    //EditText date;
    EditText time;
    EditText meetingDate;

    String email,name;

    Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invites);

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        sendInvite=mFirebaseDatabase.getReference().child("Notification");

        agoraId=findViewById(R.id.agoraId);
        message=findViewById(R.id.message);
//        date =findViewById(R.id.date);
        time=findViewById(R.id.time);

        Intent intent=getIntent();
        email = intent.getExtras().getString("email");
        name=MainActivity.user.getDisplayName();

       myCalendar = Calendar.getInstance();

        meetingDate= (EditText) findViewById(R.id.date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

    meetingDate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(SendInvites.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        meetingDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void sendInvite(View view){

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    //create new Invite
                    Invites invites=new Invites(agoraId.getText().toString(),message.getText().toString(),meetingDate.getText().toString(),time.getText().toString(),email,name);
                    sendInvite.push().setValue(invites);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        sendInvite.addListenerForSingleValueEvent(eventListener);

        Toast.makeText(this, "Invite Sent!!!!", Toast.LENGTH_SHORT).show();

        finish();
    }
}
