package com.hznhta.tickit_user.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hznhta.tickit_user.Activities.BuyTicketActivity;
import com.hznhta.tickit_user.Controllers.TicketController;
import com.hznhta.tickit_user.Models.Ticket;
import com.hznhta.tickit_user.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketListFragment extends Fragment {

    private static final String TYPE_ARG = "TicketListFragment.type";
    private static final String TAG = "TicketListFragment";

    @BindView(R.id.id_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_progress_bar)
    ProgressBar mProgressBar;

    private TicketAdapter mAdapter;

    private int mType;

    public static TicketListFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(TYPE_ARG, type);
        TicketListFragment fragment = new TicketListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, v);
        mType = getArguments().getInt(TYPE_ARG);

        mAdapter = new TicketAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        TicketController.newInstance().getTicketsList(mType, new TicketController.OnTicketsRetrievedListener() {
            @Override
            public void onTicketsRetrieved(Ticket ticket) {
                if(mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);
                mAdapter.addTicket(ticket);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNoTicketsRetrieved() {
                if(mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);
            }
        });
        return v;
    }

    private class TicketAdapter extends RecyclerView.Adapter<TicketHolder> {

        private List<Ticket> mTicketList;

        public TicketAdapter() {
            mTicketList = new ArrayList<>();
        }

        @Override
        public TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.single_ticket_layout, parent, false);
            ButterKnife.bind(this, v);
            return new TicketHolder(v);
        }

        @Override
        public void onBindViewHolder(TicketHolder holder, final int position) {
            holder.bindView(mTicketList.get(position));
        }

        public void addTicket(Ticket ticket) {
            mTicketList.add(ticket);
        }

        @Override
        public int getItemCount() {
            return mTicketList.size();
        }
    }

    private class TicketHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private Ticket mTicket;

        private TextView mTextView;

        public TicketHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.id_ticket_name);
            itemView.setOnClickListener(this);
        }

        public void bindView(Ticket ticket) {
            mTicket = ticket;
            mTextView.setText(mTicket.getName());
        }

        @Override
        public void onClick(View view) {
            startActivity(BuyTicketActivity.newIntent(getActivity(), mTicket, mType));
        }
    }
}
