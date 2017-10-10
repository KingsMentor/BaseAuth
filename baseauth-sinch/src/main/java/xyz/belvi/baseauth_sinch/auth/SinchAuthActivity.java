package xyz.belvi.baseauth_sinch.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.belvi.validator.PhoneNumberValidator;

import xyz.belvi.baseauth.auth.base.AUTH_MODE;
import xyz.belvi.baseauth.auth.base.OpenAuthActivity;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth_sinch.callbacks.SinchAuthListener;

/**
 * Created by zone2 on 10/5/17.
 */

public class SinchAuthActivity extends OpenAuthActivity {

    private SinchAuthOperations sinchAuthOperations;
    private boolean mVerificationInitiated;
    private AuthListeners.AuthResults authResults;

    public static void startSinchAuth(Context context, SinchAuthListener authListener, String apiKey, @StyleRes int styleRes) {
        AuthHandler.init(authListener);
        context.startActivity(new Intent(context, SinchAuthActivity.class)
                .putExtra(STYLE_KEY, styleRes)
                .putExtra(CODE_LENGTH, SinchAuthListener.AUTH_CODE_LENGTH)
                .putExtra(API_KEY, apiKey)
                .putExtra(AUTH_MODE_KEY, AUTH_MODE.SINCH)
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sinchAuthOperations = new SinchAuthOperations(this, getIntent().getStringExtra(API_KEY)) {

            protected void verificationInitiated() {
                mVerificationInitiated = true;
            }

            protected void timeOut() {
                authResults.timeOut();
            }

            protected void verificationFailure(Exception e) {
                authResults.verificationFailure(e);
            }

            protected void completed(String phoneNumber) {
                finish();
                AuthHandler.getsAuthListener().onAuthCompleted(phoneNumber);

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
        if (mVerificationInitiated) {
            sinchAuthOperations.signInWithCode(code);
        }
    }

    @Override
    protected void handleHelp(Context context) {
        AuthHandler.getsAuthListener().helpClicked(context);
    }

    @Override
    protected void authPhone(PhoneNumberValidator.Country selectedCountry, String phoneNumber, boolean forceResendingToken) {
        sinchAuthOperations.authPhone(selectedCountry, phoneNumber);
    }

}
