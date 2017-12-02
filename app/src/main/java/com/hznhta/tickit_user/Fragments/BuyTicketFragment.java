package com.hznhta.tickit_user.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hznhta.tickit_user.Controllers.TicketController;
import com.hznhta.tickit_user.Interfaces.OnActionCompletedListener;
import com.hznhta.tickit_user.Models.Buy;
import com.hznhta.tickit_user.Models.MovieTicket;
import com.hznhta.tickit_user.Models.ReviewRating;
import com.hznhta.tickit_user.Models.ShowTicket;
import com.hznhta.tickit_user.Models.SportsTicket;
import com.hznhta.tickit_user.Models.Ticket;
import com.hznhta.tickit_user.Models.TransportTicket;
import com.hznhta.tickit_user.R;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.id_button_review)
    Button mReviewButton;
    @BindView(R.id.id_rating_ticket)
    RatingBar mTicketRating;
    @BindView(R.id.id_review_ticket)
    EditText mTicketReview;
    @BindView(R.id.id_reviews_list)
    RecyclerView mReviewsList;

    private ReviewAdapter mAdapter;

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
        mAdapter = new ReviewAdapter();
        mReviewsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mReviewsList.setAdapter(mAdapter);

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

            mReviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReviewRating review = new ReviewRating(FirebaseAuth.getInstance().getCurrentUser().getUid(), (int) mTicketRating.getRating(), mTicketReview.getText().toString());
                    mTicketController.postTicketReview(review, mTicket, new OnActionCompletedListener() {
                        @Override
                        public void onActionSucceed() {
                            mTicketReview.setText("");
                            mTicketRating.setRating(0);
                            Snackbar.make(BuyTicketFragment.this.getView(), "Review posted!", Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onActionFailed(String err) {
                            Snackbar.make(BuyTicketFragment.this.getView(), err, Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            });

            mTicketController.fetchTicketReviews(mTicket, new TicketController.OnReviewRetrievedListener() {
                @Override
                public void onReviewRetrieved(ReviewRating review, String username) {
                    mAdapter.addReview(review, username);
                }
            });
        } else {
            Snackbar.make(BuyTicketFragment.this.getView(), "Unable to load ticket data!", Snackbar.LENGTH_LONG).show();
        }

        return v;
    }

    private class ReviewAdapter extends RecyclerView.Adapter<CommentHolder>
    {
        private List<ReviewRating> mReviewList;
        private List<String> mUserNameList;

        public ReviewAdapter() {
            mReviewList = new ArrayList<>();
            mUserNameList = new ArrayList<>();
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.single_ticket_review, viewGroup,false);
            ButterKnife.bind(this, v);
            return new CommentHolder(v);
        }

        @Override
        public void onBindViewHolder(CommentHolder commentHolder, int i) {
            commentHolder.bindView(mReviewList.get(i), mUserNameList.get(i));
        }

        public void addReview(ReviewRating obj, String username) {
            mReviewList.add(obj);
            mUserNameList.add(username);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mReviewList.size();
        }
    }


    private class CommentHolder extends RecyclerView.ViewHolder {

        private ReviewRating mReviewRating;
        private TextView mUserName;
        private RatingBar mUserRating;
        private TextView mUserReview;

        public CommentHolder(View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.id_user_name);
            mUserRating = itemView.findViewById(R.id.id_rating_user);
            mUserReview = itemView.findViewById(R.id.id_user_review);
        }

        public void bindView(ReviewRating reviewRating, String username) {
            mReviewRating = reviewRating;
            mUserRating.setRating(mReviewRating.getRating());
            mUserReview.setText(mReviewRating.getReview());
            mUserName.setText(username);
        }
    }
}
