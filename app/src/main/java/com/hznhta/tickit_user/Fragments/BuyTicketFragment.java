package com.hznhta.tickit_user.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hznhta.tickit_user.Controllers.TicketController;
import com.hznhta.tickit_user.Interfaces.OnActionCompletedListener;
import com.hznhta.tickit_user.Models.Buy;
import com.hznhta.tickit_user.Models.MovieTicket;
import com.hznhta.tickit_user.Models.ShowTicket;
import com.hznhta.tickit_user.Models.SportsTicket;
import com.hznhta.tickit_user.Models.Ticket;
import com.hznhta.tickit_user.Models.TransportTicket;
import com.hznhta.tickit_user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyTicketFragment extends Fragment {

    @BindView(R.id.id_buy_ticket_button)
    Button mBuyTicketButton;
    @BindView(R.id.id_ticket_quantity)
    EditText mTicketQuantity;
    @BindView(R.id.id_ticket_details)
    FrameLayout mTicketDetails;
    @BindView(R.id.id_input_price)
    TextView mTicketPrice;
    @BindView(R.id.id_input_date)
    TextView mTicketDate;
    @BindView(R.id.id_input_seats)
    TextView mTicketSeats;
    @BindView(R.id.id_input_place)
    TextView mTicketPlace;

    private TicketController mTicketController;

    private static final String KEY_TICKET = "BuyTicketFragment.ticket";
    private static final String KEY_TYPE = "BuyTicketFragment.type";
    private Ticket mTicket;
    private int mType;

    public static BuyTicketFragment newInstance() {
        return new BuyTicketFragment();
    }

    public static BuyTicketFragment newInstance(Ticket ticket, int type) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_TICKET, ticket);
        args.putInt(KEY_TYPE, type);
        BuyTicketFragment fragment = new BuyTicketFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTicketController = new TicketController();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ticket_all, container, false);
        ButterKnife.bind(this, v);

        if(getArguments() != null) {
            mTicket = (Ticket) getArguments().getSerializable(KEY_TICKET);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mTicket.getName());
            mTicketDate.setText(mTicket.getDateTime());
            mTicketPlace.setText(mTicket.getPlace());
            mTicketPrice.setText(mTicket.getPrice() + "");
            mTicketSeats.setText(mTicket.getSeats() > 0 ? "Available" : "Not Available");

            mType = getArguments().getInt(KEY_TYPE);
            switch (mType) {
                case Ticket.MOVIE_TICKET:
                    mTicketDetails.addView(MovieTicket.getView(getActivity()));
                    MovieTicket.populateTicket((MovieTicket) mTicket);
                    break;
                case Ticket.SHOW_TICKET:
                    mTicketDetails.addView(ShowTicket.getView(getActivity()));
                    ShowTicket.populateTicket((ShowTicket) mTicket);
                    break;
                case Ticket.SPORTS_TICKET:
                    mTicketDetails.addView(SportsTicket.getView(getActivity()));
                    SportsTicket.populateTicket((SportsTicket) mTicket);
                    break;
                case Ticket.TRANSPORT_TICKET:
                    mTicketDetails.addView(TransportTicket.getView(getActivity()));
                    TransportTicket.populateTicket((TransportTicket) mTicket);
                    break;
            }

            mBuyTicketButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = Integer.parseInt(mTicketQuantity.getText().toString());
                    if(quantity <= mTicket.getSeats()) {
                        Buy buy = new Buy(FirebaseAuth.getInstance().getCurrentUser().getEmail(), mTicket.getUid(), quantity);
                        mTicketController.buyTicket(buy, mTicket, mType, new OnActionCompletedListener() {
                            @Override
                            public void onActionSucceed() {
                                Snackbar.make(BuyTicketFragment.this.getView(), "Ticket Bought!", Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onActionFailed(String err) {

                            }
                        });
                    } else {
                        mTicketQuantity.setError("Not enough Tickets available!");
                    }
                }
            });
        }

        return v;
    }
}
