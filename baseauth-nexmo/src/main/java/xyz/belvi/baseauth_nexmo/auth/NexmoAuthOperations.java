package xyz.belvi.baseauth_nexmo.auth;

import android.app.Activity;

import com.belvi.validator.PhoneFormatException;
import com.belvi.validator.PhoneNumberValidator;
import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.SearchListener;
import com.nexmo.sdk.verify.event.UserObject;
import com.nexmo.sdk.verify.event.UserStatus;
import com.nexmo.sdk.verify.event.VerifyClientListener;
import com.nexmo.sdk.verify.event.VerifyError;

import java.io.IOException;


/**
 * Created by zone2 on 9/19/17.
 */

abstract class NexmoAuthOperations {

    private Activity activity;

    private Activity getActivity() {
        return this.activity;
    }

    private String applicationId, sharedSecretKey;

    public String getApplicationId() {
        return this.applicationId;
    }

    public String getSharedSecretKey() {
        return this.sharedSecretKey;
    }

    public NexmoAuthOperations(Activity activity, String applicationId, String sharedSecretKey) {
        this.activity = activity;
        this.applicationId = applicationId;
        this.sharedSecretKey = sharedSecretKey;
    }

    private VerifyClient mVerifyClient;

    protected void authWithNexmo(final PhoneNumberValidator.Country selectedCountry, String phoneNumber) {

        try {
            final String ccNumber = selectedCountry.toCountryCode(phoneNumber);
            NexmoClient nexmoClient = new NexmoClient.NexmoClientBuilder()
                    .context(getActivity().getApplicationContext())
                    .applicationId(getApplicationId()) //your App key
                    .sharedSecretKey(getSharedSecretKey()) //your App secret
                    .build();
            mVerifyClient = new VerifyClient(nexmoClient);
            mVerifyClient.addVerifyListener(new VerifyClientListener() {
                public void onVerifyInProgress(VerifyClient verifyClient, UserObject user) {
                    verificationInitiated();
                }

                public void onUserVerified(VerifyClient verifyClient, UserObject user) {
                    completed(ccNumber);
                }

                public void onError(VerifyClient verifyClient, VerifyError errorCode, UserObject user) {
                    timeOut();
                    verificationFailure(new Exception(errorCode.name()));
                }

                public void onException(IOException exception) {
                    verificationFailure(exception);
                }
            });
            mVerifyClient.getUserStatus("", ccNumber, new SearchListener() {
                public void onUserStatus(UserStatus userStatus) {
                    if (userStatus != UserStatus.USER_VERIFIED) {
                        mVerifyClient.getVerifiedUser("", ccNumber);
                    } else {
                        completed(ccNumber);
                    }
                }

                public void onError(VerifyError errorCode, String errorMessage) {

                }

                public void onException(IOException exception) {

                }
            });
        } catch (ClientBuilderException e) {
            e.printStackTrace();
        } catch (PhoneFormatException e) {
            e.printStackTrace();
        }


    }

    protected void signInWithCode(String code) {
        mVerifyClient.checkPinCode(code);
    }


    protected abstract void verificationInitiated();

    protected abstract void timeOut();

    protected abstract void verificationFailure(Exception e);

    protected abstract void completed(String phoneNUmber);

}
