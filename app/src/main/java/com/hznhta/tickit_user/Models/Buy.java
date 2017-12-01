package com.hznhta.tickit_user.Models;

/**
 * Created by Nokhaiz Ali on 11/26/2017.
 */

public class Buy {
    private String mEmail;
    private String mTicketId;
    private int mCount;

    public Buy(String email, String ticketId, int count) {
        mEmail = email;
        mTicketId = ticketId;
        mCount = count;
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
