package xyz.belvi.baseauth_nexmo.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.belvi.validator.PhoneNumberValidator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.UserObject;

import java.util.List;

import xyz.belvi.baseauth.auth.base.AUTH_MODE;
import xyz.belvi.baseauth.auth.base.OpenAuthActivity;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth_nexmo.callbacks.NexmoAuthListener;

/**
 * Created by zone2 on 10/5/17.
 */

public class NexmoAuthActivity extends OpenAuthActivity {

    private NexmoAuthOperations nexmoAuthOperations;
    private boolean mVerificationInitiated;
    private AuthListeners.AuthResults authResults;

    public static void startNexmoAuth(Context context, NexmoAuthListener authListener, NexmoAuth nexmoAuth, @StyleRes int styleRes) {
        AuthHandler.init(authListener);
        context.startActivity(new Intent(context, NexmoAuthActivity.class)
                .putExtra(STYLE_KEY, styleRes)
                .putExtra(CODE_LENGTH, nexmoAuth.getPinLength())
                .putExtra(API_KEY, nexmoAuth)
                .putExtra(AUTH_MODE_KEY, AUTH_MODE.NEXMO.name())
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NexmoAuth nexmoAuth = getIntent().getParcelableExtra(API_KEY);
        nexmoAuthOperations = new NexmoAuthOperations(this, nexmoAuth.getApiId(), nexmoAuth.getSharedSecret()) {

            protected void verificationInitiated() {
                mVerificationInitiated = true;
                authResults.codeSent();
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
            nexmoAuthOperations.signInWithCode(code);
        }
    }

    @Override
    protected void handleHelp(Context context) {
        AuthHandler.getsAuthListener().helpClicked(context);
    }

    @Override
    protected void authPhone(final PhoneNumberValidator.Country selectedCountry, final String phoneNumber, boolean forceResendingToken, final boolean isCallAuth) {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_PHONE_STATE)
                .withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            nexmoAuthOperations.authWithNexmo(selectedCountry, phoneNumber);
                        } else {
                            authResults.verificationFailure(new Exception("Permission not granted"));
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


}
