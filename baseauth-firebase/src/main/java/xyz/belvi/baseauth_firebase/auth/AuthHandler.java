package xyz.belvi.baseauth_firebase.auth;

import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth_firebase.callbacks.FirebaseAuthListener;

/**
 * Created by zone2 on 9/21/17.
 */

class AuthHandler {

    private static FirebaseAuthListener sAuthListener;

    public static void init(FirebaseAuthListener authListener) {
        if (sAuthListener == null)
            sAuthListener = authListener;
    }

    public static FirebaseAuthListener getsAuthListener() {
        return AuthHandler.sAuthListener;
    }
}
