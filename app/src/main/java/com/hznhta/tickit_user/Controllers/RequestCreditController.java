package com.hznhta.tickit_user.Controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hznhta.tickit_user.Interfaces.OnActionCompletedListener;

/**
 * Created by Aziz on 11/29/2017.
 */

public class RequestCreditController {


    private OnActionCompletedListener mOnActionCompletedListener;


    public static RequestCreditController newInstance() {
        return new RequestCreditController();
    }


    public void requestCredit(int amount, OnActionCompletedListener listener) {
        mOnActionCompletedListener = listener;

        FirebaseDatabase.getInstance().getReference("userCreditList").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(amount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    mOnActionCompletedListener.onActionSucceed();
                } else {
                    mOnActionCompletedListener.onActionFailed(task.getException().toString());
                }
            }
        });
    }


}
