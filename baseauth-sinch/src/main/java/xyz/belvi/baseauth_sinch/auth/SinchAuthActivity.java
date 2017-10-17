package xyz.belvi.baseauth_sinch.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;

import com.belvi.validator.PhoneNumberValidator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

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
                .putExtra(AUTH_MODE_KEY, AUTH_MODE.SINCH.name())
        );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sinchAuthOperations = new SinchAuthOperations(this, getIntent().getStringExtra(API_KEY)) {

            protected void verificationInitiated() {
                authResults.codeSent();
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
    protected void authPhone(final PhoneNumberValidator.Country selectedCountry, final String phoneNumber, boolean forceResendingToken, final boolean isCallAuth) {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE)
                .withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (isCallAuth) {
                                sinchAuthOperations.authPhoneViaCall(selectedCountry, phoneNumber);
                            } else {
                                sinchAuthOperations.authPhoneViaSMS(selectedCountry, phoneNumber);
                            }
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
