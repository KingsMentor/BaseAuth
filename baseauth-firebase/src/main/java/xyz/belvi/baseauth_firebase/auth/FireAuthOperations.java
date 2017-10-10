package xyz.belvi.baseauth_firebase.auth;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.belvi.validator.PhoneFormatException;
import com.belvi.validator.PhoneNumberValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 * Created by zone2 on 9/19/17.
 */

abstract class FireAuthOperations {

    private Activity activity;

    private Activity getActivity() {
        return this.activity;
    }

    public FireAuthOperations(Activity activity) {
        this.activity = activity;
    }

    protected void authPhone(PhoneNumberValidator.Country selectedCountry, String phoneNumber) {
        authPhone(selectedCountry, phoneNumber, null);
    }

    private String mPhoneNumber;

    protected void authPhone(PhoneNumberValidator.Country selectedCountry, String phoneNumber, PhoneAuthProvider.ForceResendingToken resendingToken) {
        try {
            mPhoneNumber = selectedCountry.toCountryCode(phoneNumber);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    getActivity(),               // Activity (for callback binding)
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            completed(phoneAuthCredential, mPhoneNumber);

                        }

                        @Override
                        public void onCodeAutoRetrievalTimeOut(String s) {
                            super.onCodeAutoRetrievalTimeOut(s);
                            timeOut();
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            verificationFailure(e);
                        }

                        @Override
                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            codeSent(s, forceResendingToken);


                        }
                    }, resendingToken);

        } catch (PhoneFormatException e) {
            e.printStackTrace();
        }

    }

    protected void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            completed(credential, mPhoneNumber);
                        } else {
                            verificationFailure(task.getException());

                        }
                    }
                });
    }


    protected abstract void codeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken);

    protected abstract void timeOut();

    protected abstract void verificationFailure(Exception e);

    protected abstract void completed(PhoneAuthCredential phoneAuthCredential, String phoneNumber);

}
