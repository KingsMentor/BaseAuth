package xyz.belvi.baseauth.auth.base;

import android.content.Context;

import xyz.belvi.validator.PhoneNumberValidator;

import xyz.belvi.baseauth.callbacks.AuthListeners;

/**
 * Created by zone2 on 10/6/17.
 */

public class OpenAuthActivity extends AuthActivity {

    public static final String CODE_LENGTH = " xyz.belvi.baseauth.auth.base.CODE_LENGTH";
    public static final String API_KEY = " xyz.belvi.baseauth.auth.base.API_KEY";
    protected int RC_HANDLE_PERM = 111;

    protected void handleHelp(Context context) {

    }

    protected void bindAuthResult(AuthListeners.AuthResults auths) {

    }

    protected void manualAuth(String code) {

    }

    protected void authPhone(PhoneNumberValidator.Country selectedCountry, String phoneNumber, boolean forceResendingToken, boolean callAuth) {

    }
}
