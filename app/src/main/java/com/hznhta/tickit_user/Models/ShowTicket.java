package com.hznhta.tickit_user.Models;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hznhta.tickit_user.R;

public class ShowTicket extends Ticket {
    private String endingTime;

    private static final String TAG = "ShowTicket";

    private static TextView sEndingTime;

    public ShowTicket() {
        super();
        this.endingTime = null;
    }

    public ShowTicket(String name, int price, int seats, String place, String dateTime, String endingTime) {
        super(name, price, seats, place, dateTime);
        this.endingTime = endingTime;
    }

    public static void populateTicket(ShowTicket ticket) {
        sEndingTime.setText(ticket.getEndingTime());
    }

    public static View getView(final Context context) {
        View v = getTicketView(context, SHOW_TICKET);

        sEndingTime = v.findViewById(R.id.id_input_end_time);

        return v;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }
}
