package xyz.belvi.baseauth_firebase.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import appzonegroup.com.phonenumberverifier.PhoneFormatException;
import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;
import xyz.belvi.baseauth.auth.base.OpenAuthActivity;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth_firebase.callbacks.FirebaseAuthListener;

/**
 * Created by zone2 on 10/5/17.
 */

public class FireAuthActivity extends OpenAuthActivity {

    private FireAuthOperations fireAuthOperations;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    PhoneNumberVerifier.Countries selectedCountry;
    private String mVerificationId, phone;
    private AuthListeners.AuthResults authResults;

    public static void startFirebasePhoneAuth(Context context, FirebaseAuthListener authListener, @StyleRes int styleRes) {
        AuthHandler.init(authListener);
        context.startActivity(new Intent(context, FireAuthActivity.class)
                .putExtra(STYLE_KEY, styleRes)
                .putExtra(CODE_LENGTH, FirebaseAuthListener.FIREBASE_CODE_LENGTH)
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireAuthOperations = new FireAuthOperations(this) {
            protected void codeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mForceResendingToken = forceResendingToken;
                mVerificationId = verificationId;
                authResults.codeSent();
            }

            protected void timeOut() {
                authResults.timeOut();
            }

            protected void verificationFailure(Exception e) {
                authResults.verificationFailure(e);
            }

            protected void completed(PhoneAuthCredential phoneAuthCredential) {

                try {
                    AuthHandler.getsAuthListener().onAuthCompleted(phoneAuthCredential, selectedCountry.ToCountryCode(selectedCountry, phone));
                } catch (PhoneFormatException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0)
            super.onBackPressed();
        else {
            finish();
            AuthHandler.getsAuthListener().authIgnored();
        }
    }

    @Override
    protected void bindAuthResult(AuthListeners.AuthResults authResults) {
        this.authResults = authResults;
    }

    @Override
    protected void manualAuth(String code) {
        if (!mVerificationId.isEmpty()) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            fireAuthOperations.signInWithPhoneAuthCredential(credential);
        }
    }

    @Override
    protected void handleHelp(Context context) {
        AuthHandler.getsAuthListener().helpClicked(context);
    }

    @Override
    protected void authPhone(PhoneNumberVerifier.Countries selectedCountry, String phoneNumber, boolean forceResendingToken) {
        this.selectedCountry = selectedCountry;
        this.phone = phoneNumber;
        if (forceResendingToken) {
            fireAuthOperations.authPhone(selectedCountry, phoneNumber, mForceResendingToken);
        } else {
            fireAuthOperations.authPhone(selectedCountry, phoneNumber);
        }
    }

}
