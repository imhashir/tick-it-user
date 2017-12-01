package com.hznhta.tickit_user.Fragments;

/**
 * Created by tarviha on 11/27/2017.
 */

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hznhta.tickit_user.Activities.NavViewActivity;
import com.hznhta.tickit_user.Activities.SignUpActivity;
import com.hznhta.tickit_user.Controllers.AccountsController;
import com.hznhta.tickit_user.Interfaces.OnActionCompletedListener;
import com.hznhta.tickit_user.R;

import butterknife.ButterKnife;
import butterknife.BindView;

public class SignInFragment extends Fragment {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.btn_sign_up) Button _signupButton;
    private ProgressDialog mProgressDialog;

    private AccountsController mAccountsController;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    private View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountsController = new AccountsController();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.user_login_fragment,container, false);
        ButterKnife.bind(this, v);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        mProgressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        mAccountsController.signInUser(email, password, new OnActionCompletedListener() {
            @Override
            public void onActionSucceed() {
                mProgressDialog.dismiss();
                Intent i = NavViewActivity.newIntent(getActivity(), null);
                startActivity(i);
                getActivity().finish();
            }

            @Override
            public void onActionFailed(String err) {
                mProgressDialog.dismiss();
                onLoginFailed();
            }
        });
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}