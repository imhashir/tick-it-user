package com.hznhta.tickit_user.Models;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hznhta.tickit_user.R;

public class TransportTicket extends Ticket {

    private String source;
    private String destination;
    private String arrivalTime;

    private static final String TAG = "TransportTicket";

    private static EditText sSource;
    private static EditText sDestination;
    private static EditText sArrivalTime;
    private static Button sAddButton;

    public TransportTicket() {
        super();
        source = destination = arrivalTime = null;
    }

    public TransportTicket(String name, int price, int seats, String place, String dateTime, String source, String destination, String arrivalTime) {
        super(name, price, seats, place, dateTime);
        this.source = source;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
    }

    public static void populateTicket(TransportTicket ticket) {
        populateTicketView(ticket);
        sSource.setText(ticket.getSource() + "");
        sDestination.setText(ticket.getDestination() + "");
        sArrivalTime.setText(ticket.getArrivalTime() + "");
        sAddButton.setText("Update");
    }

    public static View getView(final Context context) {
        View v = getTicketView(context, TRANSPORT_TICKET);

        sSource = v.findViewById(R.id.id_input_source);
        sDestination = v.findViewById(R.id.id_input_dest);
        sArrivalTime = v.findViewById(R.id.id_input_arrival_time);
        sAddButton = v.findViewById(R.id.id_add_ticket_button);

        return v;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
