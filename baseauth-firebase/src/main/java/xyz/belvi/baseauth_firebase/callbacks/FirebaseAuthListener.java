package xyz.belvi.baseauth_firebase.callbacks;

import com.google.firebase.auth.PhoneAuthCredential;

import xyz.belvi.baseauth.callbacks.AuthListeners;

/**
 * Created by zone2 on 9/21/17.
 */

public interface FirebaseAuthListener extends AuthListeners {
    int FIREBASE_CODE_LENGTH = 6;
    void onAuthCompleted(PhoneAuthCredential credential, String phoneNumber);
}


