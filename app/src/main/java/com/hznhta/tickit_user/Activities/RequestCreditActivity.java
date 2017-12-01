package com.hznhta.tickit_user.Activities;

import android.support.v4.app.Fragment;

import com.hznhta.tickit_user.Fragments.RequestCreditFragment;
import com.hznhta.tickit_user.SingleFragmentActivity;


public class RequestCreditActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return RequestCreditFragment.newInstance();
    }
}
