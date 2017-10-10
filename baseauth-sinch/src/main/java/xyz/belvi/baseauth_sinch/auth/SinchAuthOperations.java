package xyz.belvi.baseauth_sinch.auth;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.belvi.validator.PhoneFormatException;
import com.belvi.validator.PhoneNumberValidator;
import com.sinch.verification.Config;
import com.sinch.verification.InitiationResult;
import com.sinch.verification.PhoneNumberUtils;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;

import java.util.concurrent.TimeUnit;


/**
 * Created by zone2 on 9/19/17.
 */

abstract class SinchAuthOperations {

    private Activity activity;

    private Activity getActivity() {
        return this.activity;
    }

    private String applicationKey;

    public String getApplicationKey() {
        return this.applicationKey;
    }

    public SinchAuthOperations(Activity activity, String applicationKey) {
        this.applicationKey = applicationKey;
        this.activity = activity;
    }

    private Verification mVerification;

    protected void authPhone(PhoneNumberValidator.Country selectedCountry, String phoneNumber) {
        Config config = SinchVerification.config().applicationKey(getApplicationKey()).context(getActivity()).build();
        try {
            final String phoneNumberInE164 = selectedCountry.toCountryCode(phoneNumber);
            mVerification = SinchVerification.createSmsVerification(config, phoneNumberInE164, new VerificationListener() {
                public void onInitiated(InitiationResult initiationResult) {
                    verificationInitiated();
                }

                public void onInitiationFailed(Exception e) {
                    timeOut();
                    verificationFailure(e);
                }

                public void onVerified() {
                    completed(phoneNumberInE164);
                }

                public void onVerificationFailed(Exception e) {
                    verificationFailure(e);
                }
            });
//            mVerification.initiate();
        } catch (PhoneFormatException e) {
            e.printStackTrace();
        }


    }

    protected void signInWithCode(String code) {
        mVerification.verify(code);
    }


    protected abstract void verificationInitiated();

    protected abstract void timeOut();

    protected abstract void verificationFailure(Exception e);

    protected abstract void completed(String phoneNumber);

}
