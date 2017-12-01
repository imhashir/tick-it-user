package com.hznhta.tickit_user.Controllers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.hznhta.tickit_user.Models.MovieTicket;
import com.hznhta.tickit_user.Models.ShowTicket;
import com.hznhta.tickit_user.Models.SportsTicket;
import com.hznhta.tickit_user.Models.Ticket;
import com.hznhta.tickit_user.Models.TransportTicket;

public class TicketController {

    private static final String[] types = {"movie", "show", "transport", "sports"};

    private OnTicketsRetrievedListener mOnTicketsRetrievedListener;

    public interface OnTicketsRetrievedListener {
        void onTicketsRetrieved(Ticket ticket);
    }

    public static TicketController newInstance() {
        return new TicketController();
    }

    public void getTicketsList(final int type, OnTicketsRetrievedListener listener) {
        mOnTicketsRetrievedListener = listener;

        FirebaseDatabase.getInstance().getReference("tickets").child(types[type]).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Ticket ticket = null;
                switch (type) {
                    case Ticket.MOVIE_TICKET:
                        ticket = dataSnapshot.getValue(MovieTicket.class);
                        break;
                    case Ticket.SHOW_TICKET:
                        ticket = dataSnapshot.getValue(ShowTicket.class);
                        break;
                    case Ticket.SPORTS_TICKET:
                        ticket = dataSnapshot.getValue(SportsTicket.class);
                        break;
                    case Ticket.TRANSPORT_TICKET:
                        ticket = dataSnapshot.getValue(TransportTicket.class);
                        break;
                }
                ticket.setUid(dataSnapshot.getKey());
                mOnTicketsRetrievedListener.onTicketsRetrieved(ticket);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
