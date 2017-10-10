package xyz.belvi.baseauth_firebase.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;

import com.belvi.validator.PhoneFormatException;
import com.belvi.validator.PhoneNumberValidator;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import xyz.belvi.baseauth.auth.base.AUTH_MODE;
import xyz.belvi.baseauth.auth.base.OpenAuthActivity;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth_firebase.callbacks.FirebaseAuthListener;

/**
 * Created by zone2 on 10/5/17.
 */

public class FireAuthActivity extends OpenAuthActivity {

    private FireAuthOperations fireAuthOperations;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    private String mVerificationId;
    private AuthListeners.AuthResults authResults;

    public static void startFirebasePhoneAuth(Context context, FirebaseAuthListener authListener, @StyleRes int styleRes) {
        AuthHandler.init(authListener);
        context.startActivity(new Intent(context, FireAuthActivity.class)
                .putExtra(STYLE_KEY, styleRes)
                .putExtra(CODE_LENGTH, FirebaseAuthListener.AUTH_CODE_LENGTH)
                .putExtra(AUTH_MODE_KEY, AUTH_MODE.FIRE_BASE.name())
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

            protected void completed(PhoneAuthCredential phoneAuthCredential, String phoneNUmber) {
                finish();
                AuthHandler.getsAuthListener().onAuthCompleted(phoneAuthCredential, phoneNUmber);

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
    protected void authPhone(final PhoneNumberValidator.Country selectedCountry, final String phoneNumber, final boolean resendingToken, boolean isCallAuth) {
        if (resendingToken) {
            fireAuthOperations.authPhone(selectedCountry, phoneNumber, mForceResendingToken);
        } else {
            fireAuthOperations.authPhone(selectedCountry, phoneNumber);
        }


    }


}
