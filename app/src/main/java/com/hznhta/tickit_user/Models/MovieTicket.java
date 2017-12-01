package com.hznhta.tickit_user.Models;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hznhta.tickit_user.R;

public class MovieTicket extends Ticket {

    private static final String TAG = "MovieTicket";

    private String genre;
    private int length;

    private static EditText sMovieGenre;
    private static EditText sMovieLength;
    private static Button sAddButton;

    public MovieTicket() {
        super();
        genre = null;
        length = 0;
    }

    public MovieTicket(String name, int price, int seats, String place, String dateTime, String genre, int length) {
        super(name, price, seats, place, dateTime);
        this.genre = genre;
        this.length = length;
    }

    public static void populateTicket(MovieTicket ticket) {
        populateTicketView(ticket);
        sMovieGenre.setText(ticket.getGenre() + "");
        sMovieLength.setText(ticket.getLength() + "");
        sAddButton.setText("Update");
    }

    public static View getView(final Context context) {
        View v = getTicketView(context, MOVIE_TICKET);

        sMovieGenre = v.findViewById(R.id.id_input_genre);
        sMovieLength = v.findViewById(R.id.id_input_length);
        sAddButton = v.findViewById(R.id.id_add_ticket_button);

        return v;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
