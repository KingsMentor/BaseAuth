package xyz.belvi.baseauth.auth;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import appzonegroup.com.phonenumberverifier.PhoneFormatException;
import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;

/**
 * Created by zone2 on 9/19/17.
 */

public abstract class AuthFragment extends Fragment {

    protected void authPhone(PhoneNumberVerifier.Countries selectedCountry, String phoneNumber) {
        authPhone(selectedCountry, phoneNumber, null);
    }

    protected void authPhone(PhoneNumberVerifier.Countries selectedCountry, String phoneNumber, PhoneAuthProvider.ForceResendingToken resendingToken) {
        try {
            phoneNumber = selectedCountry.ToCountryCode(selectedCountry,
                    phoneNumber);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    getActivity(),               // Activity (for callback binding)
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            completed(phoneAuthCredential);

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

                            completed(credential);
                        } else {
                            verificationFailure(task.getException());

                        }
                    }
                });
    }


    protected abstract void codeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken);

    protected abstract void timeOut();

    protected abstract void verificationFailure(Exception e);

    protected abstract void completed(PhoneAuthCredential phoneAuthCredential);

}
