package com.hznhta.tickit_user.Controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hznhta.tickit_user.Interfaces.OnActionCompletedListener;
import com.hznhta.tickit_user.Models.User;

public class AccountsController {

    private OnActionCompletedListener mOnActionCompletedListener;

    public static AccountsController newInstance() {
        return new AccountsController();
    }

    public void signUpUser(final String name, final String email, String password, final String address, OnActionCompletedListener listener) {
        mOnActionCompletedListener = listener;
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReference("users");
                    User user = new User(name, email, address);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                mOnActionCompletedListener.onActionSucceed();
                            } else {
                                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseAuth.getInstance().signOut();
                                        mOnActionCompletedListener.onActionFailed(task.getException().toString());
                                    }
                                });
                            }
                        }
                    });
                } else {
                    mOnActionCompletedListener.onActionFailed(task.getException().toString());
                }
            }
        });
    }

    public void signInUser(String email, String password, OnActionCompletedListener listener) {
        mOnActionCompletedListener = listener;
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mOnActionCompletedListener.onActionSucceed();
                        } else {
                            mOnActionCompletedListener.onActionFailed("Invalid Credentials!");
                        }
                    }
                });
    }

}
