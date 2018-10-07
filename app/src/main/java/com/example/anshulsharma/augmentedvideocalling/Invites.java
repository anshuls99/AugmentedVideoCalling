package com.example.anshulsharma.augmentedvideocalling;

public class Invites {

    private String mId;
    private String mMessage;
    private String mDate;
    private String mTime;
    private String mEmail;
    private String mName;

    public Invites() {
    }

    public Invites(String Id, String Message, String Date,String Time,String Email,String Name){
        mId=Id;
        mMessage=Message;
        mDate=Date;
        mTime=Time;
        mEmail=Email;
        mName=Name;
    }

    public String getmId() {
        return mId;
    }

    public String getmMessage() {
        return mMessage;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmName() {
        return mName;
    }
}
