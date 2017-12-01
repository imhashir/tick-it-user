package com.hznhta.tickit_user.Activities;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.hznhta.tickit_user.Fragments.SignInFragment;
import com.hznhta.tickit_user.SingleFragmentActivity;

public class SignInActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return SignInFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, SignInActivity.class);
        return i;
    }
}