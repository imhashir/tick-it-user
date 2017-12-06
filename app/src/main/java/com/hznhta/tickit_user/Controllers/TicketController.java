package com.hznhta.tickit_user.Controllers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hznhta.tickit_user.Interfaces.OnActionCompletedListener;
import com.hznhta.tickit_user.Models.Buy;
import com.hznhta.tickit_user.Models.MovieTicket;
import com.hznhta.tickit_user.Models.ReviewRating;
import com.hznhta.tickit_user.Models.ShowTicket;
import com.hznhta.tickit_user.Models.SportsTicket;
import com.hznhta.tickit_user.Models.Ticket;
import com.hznhta.tickit_user.Models.TransportTicket;

public class TicketController {

    private static final String[] types = {"movie", "show", "transport", "sports"};

    private OnTicketsRetrievedListener mOnTicketsRetrievedListener;
    private OnActionCompletedListener mOnActionCompletedListener;
    private OnReviewRetrievedListener mOnReviewRetrievedListener;

    private FirebaseDatabase mDatabase;
    private String mCurrentUserId;

    private static final String TAG = "TicketController";

    public interface OnTicketsRetrievedListener {
        void onTicketsRetrieved(Ticket ticket);
        void onNoTicketsRetrieved();
    }

    public interface OnReviewRetrievedListener {
        void onReviewRetrieved(ReviewRating review, String username);
    }

    public TicketController() {
        mDatabase = FirebaseDatabase.getInstance();
        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static TicketController newInstance() {
        return new TicketController();
    }

    public void getTicketsList(final int type, OnTicketsRetrievedListener listener) {
        mOnTicketsRetrievedListener = listener;
        mDatabase.getReference("tickets").child(types[type]).addChildEventListener(new ChildEventListener() {
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
        mDatabase.getReference("tickets").child(types[type]).orderByKey().limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0)
                    mOnTicketsRetrievedListener.onNoTicketsRetrieved();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buyTicket(final Buy buy, final Ticket ticket, final int type, OnActionCompletedListener listener) {
        mOnActionCompletedListener = listener;
        mDatabase.getReference("userCreditAccounts").child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    final Long accountBalance = ((Long) dataSnapshot.getValue());
                    final int totalBill = ticket.getPrice() * buy.getCount();
                    if (accountBalance > totalBill) {
                        mDatabase.getReference("buy").push().setValue(buy).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mDatabase.getReference("tickets").child(types[type]).child(ticket.getUid()).child("seats").setValue(ticket.getSeats() - buy.getCount()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mDatabase.getReference("userTickets").child(mCurrentUserId).child(ticket.getUid()).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDatabase.getReference("userCreditAccounts").child(mCurrentUserId).setValue(accountBalance - totalBill).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                mOnActionCompletedListener.onActionSucceed();
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                mOnActionCompletedListener.onActionFailed(task.getException().toString());
                                            }
                                        }
                                    });
                                } else {
                                    mOnActionCompletedListener.onActionFailed(task.getException().toString());
                                }
                            }
                        });
                    } else {
                        mOnActionCompletedListener.onActionFailed("Not enough credit in account");
                    }
                } else {
                    mOnActionCompletedListener.onActionFailed("Not enough credit in account");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void postTicketReview(final ReviewRating review, final Ticket ticket, OnActionCompletedListener listener) {
        mOnActionCompletedListener = listener;
        mDatabase.getReference("userTickets").child(mCurrentUserId).child(ticket.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    mDatabase.getReference("review").child(ticket.getUid()).child(review.getUserId()).setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                mOnActionCompletedListener.onActionSucceed();
                            else {
                                mOnActionCompletedListener.onActionFailed("Failed to post Review!");
                                Log.wtf(TAG, task.getException());
                            }
                        }
                    });
                } else {
                    mOnActionCompletedListener.onActionFailed("Ticket not bought!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchTicketReviews(Ticket ticket, OnReviewRetrievedListener listener) {
        mOnReviewRetrievedListener = listener;
        FirebaseDatabase.getInstance().getReference("review").child(ticket.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final ReviewRating review = dataSnapshot.getValue(ReviewRating.class);
                mDatabase.getReference("users").child(review.getUserId()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mOnReviewRetrievedListener.onReviewRetrieved(review, dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
