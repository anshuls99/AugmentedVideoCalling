package com.example.anshulsharma.augmentedvideocalling;

public class Contacts {

    private String mName;
    private String mEmail;
    private String mPosts;
    private String mStatus;
    private String mDate;

    public Contacts() {
    }

    public Contacts(String name, String email){
       mName=name;
       mEmail=email;
    }

    public Contacts(String name,String posts,String status,String date){
        mName=name;
        mPosts=posts;
        mStatus=status;
        mDate=date;
    }


    public String getmName() {
        return mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmPosts() {
        return mPosts;
    }

    public String getmStatus() {
        return mStatus;
    }

    public String getmDate() {
        return mDate;
    }
}
