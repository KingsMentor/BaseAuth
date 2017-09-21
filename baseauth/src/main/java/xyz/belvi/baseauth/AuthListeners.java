package xyz.belvi.baseauth;

import com.google.firebase.auth.PhoneAuthCredential;

import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;

/**
 * Created by zone2 on 9/21/17.
 */

public interface AuthListeners {
    public interface FirebaseAuthListener {
        void onAuthCompleted(PhoneAuthCredential credential);
    }
}
