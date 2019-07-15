package com.zohar.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean solved;
    private String msuspend;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(UUID id){
        this.mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getMsuspend() {
        return msuspend;
    }

    public void setMsuspend(String msuspend) {
        this.msuspend = msuspend;
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }
}
