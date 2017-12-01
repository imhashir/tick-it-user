package com.hznhta.tickit_user.Models;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hznhta.tickit_user.R;

public class SportsTicket extends Ticket {

    private String team1;
    private String team2;

    private static TextView sNameTeam1;
    private static TextView sNameTeam2;

    private static final String TAG = "SportsTicket";

    public SportsTicket() {
        super();
        team1 = team2 = null;
    }

    public SportsTicket(String name, int price, int seats, String place, String dateTime, String team1, String team2) {
        super(name, price, seats, place, dateTime);
        this.team1 = team1;
        this.team2 = team2;
    }

    public static void populateTicket(SportsTicket ticket) {
        sNameTeam1.setText(ticket.getTeam1() + "");
        sNameTeam2.setText(ticket.getTeam2() + "");
    }

    public static View getView(final Context context) {
        View v = getTicketView(context, SPORTS_TICKET);

        sNameTeam1 = v.findViewById(R.id.id_input_team1);
        sNameTeam2 = v.findViewById(R.id.id_input_team2);

        return v;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }
}
