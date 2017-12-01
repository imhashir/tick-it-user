package com.hznhta.tickit_user.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hznhta.tickit_user.Fragments.BuyTicketFragment;
import com.hznhta.tickit_user.Models.Ticket;
import com.hznhta.tickit_user.SingleFragmentActivity;

public class BuyTicketActivity extends SingleFragmentActivity {

    private static final String KEY_TICKET = "BuyTicketActivity.ticket";
    private static final String KEY_TYPE = "BuyTicketActivity.type";

    public static Intent newIntent(Context context, Ticket ticket, int type) {
        Intent i = new Intent(context, BuyTicketActivity.class);
        i.putExtra(KEY_TYPE, type);
        i.putExtra(KEY_TICKET, ticket);
        return i;
    }

    @Override
    public Fragment createFragment() {
        return BuyTicketFragment.newInstance((Ticket) getIntent().getExtras().getSerializable(KEY_TICKET), getIntent().getExtras().getInt(KEY_TYPE));
    }
}
