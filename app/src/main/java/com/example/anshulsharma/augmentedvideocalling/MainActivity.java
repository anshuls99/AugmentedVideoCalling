package com.example.anshulsharma.augmentedvideocalling;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
//import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import in.slanglabs.platform.application.ISlangApplicationStateListener;
import in.slanglabs.platform.application.SlangApplication;
import in.slanglabs.platform.application.SlangApplicationUninitializedException;
import in.slanglabs.platform.application.actions.DefaultResolvedIntentAction;
import in.slanglabs.platform.session.SlangResolvedIntent;
import in.slanglabs.platform.session.SlangSession;
import in.slanglabs.platform.ui.SlangScreenContext;

public class MainActivity extends AppCompatActivity  {

    private static final int RC_SIGN_IN = 0;
   private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    public static FirebaseUser user;

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference loginedUser;
    public DatabaseReference userPosts;

    ViewPager viewPager;
    TabLayout tabLayout;

    public DatabaseReference notification;

    ProgressDialog progressDialog;

    private ChildEventListener mChildEventListener;
    public ChildEventListener mPostChildEventListener;
    public ChildEventListener mNotificationChildEventListner;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());


    public static String POSITION = "POSITION";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.man);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();


        SlangApplication.initialize(MainActivity.this, "81fdb1c4bb3747afbe568e933fb17b74", "08271cce6b7b4e7b873934727e39b06e", new ISlangApplicationStateListener() {
            public void onInitialized() {
                // Slang has successfully initialized, now register actions
                try {
                    SlangApplication.getIntentDescriptor( "add_post").setResolutionAction(new DefaultResolvedIntentAction() {
                        public SlangSession.Status action(SlangResolvedIntent intent, SlangSession session) {

                            final Activity activity = SlangScreenContext.getInstance().getCurrentActivity();

                            Intent i = new Intent((Activity) activity, AddPost.class);
                            ((Activity) activity).startActivity(i);
                            // Complete the action for this intent

                            return session.success();
                        }
                    });

                    SlangApplication.getIntentDescriptor( "send_invites").setResolutionAction(new DefaultResolvedIntentAction() {
                        public SlangSession.Status action(SlangResolvedIntent intent, SlangSession session) {

                            final Activity activity = SlangScreenContext.getInstance().getCurrentActivity();

                            Intent i = new Intent((Activity) activity, SendInvites.class);

                            String email = intent.getEntity("email").getValue();

                            i.putExtra("email",email);
                            ((Activity) activity).startActivity(i);
                            // Complete the action for this intent

                            return session.success();
                        }
                    });

                } catch (SlangApplicationUninitializedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onInitializationFailed(FailureReason failureReason) {
                Toast.makeText(MainActivity.this, "Not able to initialize Slang", Toast.LENGTH_LONG).show();
            }
        });




        mFirebaseDatabase=FirebaseDatabase.getInstance();

        loginedUser=mFirebaseDatabase.getReference().child("User");

        userPosts=mFirebaseDatabase.getReference().child("Posts");

        notification=mFirebaseDatabase.getReference().child("Notification");

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        loginedUser.keepSynced(true);

        userPosts.keepSynced(true);

        notification.keepSynced(true);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Toast.makeText(MainActivity.this, "You r signed in", Toast.LENGTH_SHORT).show();
                    onSignedInInitialize();
                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void onSignedOutCleanup() {

        try {
            ContactsFragment.adapter.clear();
            NotificationFragment.adapter.clear();
            HomeFragment.homeAdapter.clear();
        }catch(Exception e){
            e.printStackTrace();
        }
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            loginedUser.removeEventListener(mChildEventListener);
            userPosts.removeEventListener(mPostChildEventListener);
            notification.removeEventListener(mNotificationChildEventListner);
            mChildEventListener = null;
            mNotificationChildEventListner=null;
            mPostChildEventListener=null;
        }
    }


    private void onSignedInInitialize() {
        attachContactsDatabaseReadListener();
        attachPostsDatabaseReadListener();
        attachNotificationDatabaseReadListener();
    }

    public void LoadPosts (DataSnapshot dataSnapshot){
        Contacts contacts = dataSnapshot.getValue(Contacts.class);
        if(contacts.getmStatus().equals("Private") && (!contacts.getmEmail().equals(user.getEmail())));
        else {
            HomeFragment.homecontacts.add(contacts);
        }

    }

    public void LoadContacts (DataSnapshot dataSnapshot){

        Contacts contacts = dataSnapshot.getValue(Contacts.class);
        if(contacts.getmEmail().equals(user.getEmail()));
        else
            ContactsFragment.contacts.add(contacts);

    }

    public void LoadNotification (DataSnapshot dataSnapshot){

        Invites invites = dataSnapshot.getValue(Invites.class);
        if(!invites.getmEmail().equals(user.getEmail()));
        else
            NotificationFragment.invites.add(invites);

    }

    private void attachPostsDatabaseReadListener() {

        if (mPostChildEventListener == null) {
            mPostChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
//                    Contacts contacts = dataSnapshot.getValue(Contacts.class);
//                    if(contacts.getmStatus().equals("Private") && (!contacts.getmEmail().equals(user.getEmail())));
//                    else {
//                        HomeFragment.homecontacts.add(contacts);
//                    }
                    progressDialog.dismiss();
                    LoadPosts(dataSnapshot);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            userPosts.addChildEventListener(mPostChildEventListener);
        }
    }

    private void attachNotificationDatabaseReadListener() {

        if (mNotificationChildEventListner == null) {
            mNotificationChildEventListner = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
//                    Invites invites = dataSnapshot.getValue(Invites.class);
//                    if(!invites.getmEmail().equals(user.getEmail()));
//                    else
//                    NotificationFragment.invites.add(invites);
                    LoadNotification(dataSnapshot);
                    progressDialog.dismiss();
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            notification.addChildEventListener(mNotificationChildEventListner);
        }
    }


    private void attachContactsDatabaseReadListener() {

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
//                    Contacts contacts = dataSnapshot.getValue(Contacts.class);
//                    if(contacts.getmEmail().equals(user.getEmail()));
//                    else
//                    ContactsFragment.contacts.add(contacts);
                    LoadContacts(dataSnapshot);
                    progressDialog.dismiss();
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            loginedUser.addChildEventListener(mChildEventListener);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            //create new user
                            Contacts contacts = new Contacts(user.getDisplayName(),user.getEmail());
                            loginedUser.push().setValue(contacts);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                loginedUser.addListenerForSingleValueEvent(eventListener);

                Toast.makeText(this, "Welcome "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthListener);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
            ContactsFragment.adapter.clear();
        try {
            NotificationFragment.adapter.clear();
        }catch (Exception e){
            e.printStackTrace();
        }
            HomeFragment.homeAdapter.clear();


        detachDatabaseReadListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
