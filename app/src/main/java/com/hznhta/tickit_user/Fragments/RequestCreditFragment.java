package com.hznhta.tickit_user.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hznhta.tickit_user.Controllers.RequestCreditController;
import com.hznhta.tickit_user.Interfaces.OnActionCompletedListener;
import com.hznhta.tickit_user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestCreditFragment extends Fragment {
    private static final String TAG = "RequestCreditFragment";

    @BindView(R.id.id_request_credit_button) Button mRequestCreditButton;
    @BindView(R.id.id_amount) EditText mAmountCredit;

    public static Fragment newInstance() {
        return new RequestCreditFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_request_credit, container, false);
        ButterKnife.bind(this, v);

        mRequestCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestCreditController.newInstance().requestCredit(Integer.parseInt(mAmountCredit.getText().toString()), new OnActionCompletedListener() {
                    @Override
                    public void onActionSucceed() {
                        mAmountCredit.setText("");
                        Snackbar.make(RequestCreditFragment.this.getView(), "Credit Request Sent!", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onActionFailed(String err) {
                        Snackbar.make(RequestCreditFragment.this.getView(), "Unable to send Credit Request!", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        return v;
    }
}