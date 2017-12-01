package com.hznhta.tickit_user.Models;

/**
 * Created by Nokhaiz Ali on 11/26/2017.
 */

public class Buy {
    private String mId;
    private String mEmail;
    private String mTicketId;
    private int mCount;

    public void setId(String id) {
        this.mId = id;
    }

    public Buy(String id, String email, String ticketId, int count) {
        mId = id;
        mEmail = email;
        mTicketId = ticketId;
        mCount = count;
    }

    public String getId() {
        return mId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getTicketId() {
        return mTicketId;
    }

    public void setTicketId(String ticketId) {
        mTicketId = ticketId;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }
}
