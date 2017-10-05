package xyz.belvi.baseauth.callbacks;

import android.app.Activity;

import com.google.firebase.auth.PhoneAuthCredential;

import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;

/**
 * Created by zone2 on 9/21/17.
 */

public interface AuthListeners {
    interface AUTH_CODE_LENGTH {
        int FIREBASE_CODE_LENGTH = 6;
    }

    interface FirebaseAuthListener extends AuthListeners {
        void onAuthCompleted(PhoneAuthCredential credential, String phoneNumber);
    }

    void authIgnored();

    void helpClicked(Activity activity);
}
