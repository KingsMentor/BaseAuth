package xyz.belvi.baseauth.callbacks;

import com.google.firebase.auth.PhoneAuthCredential;

import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;

/**
 * Created by zone2 on 9/21/17.
 */

public interface AuthListeners {

    interface FirebaseAuthListener extends AuthListeners {
        void onAuthCompleted(PhoneAuthCredential credential, String phoneNumber);
    }

    void authIgnored();
}
