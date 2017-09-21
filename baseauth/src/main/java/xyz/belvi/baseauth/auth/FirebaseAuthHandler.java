package xyz.belvi.baseauth.auth;

import xyz.belvi.baseauth.AuthListeners;
import xyz.belvi.baseauth.AuthListeners.FirebaseAuthListener;

/**
 * Created by zone2 on 9/21/17.
 */

class FirebaseAuthHandler {

    private static AuthListeners.FirebaseAuthListener sAuthListener;

    public static void init(AuthListeners.FirebaseAuthListener authListener) {
        if (sAuthListener == null)
            sAuthListener = authListener;
    }

    public static FirebaseAuthListener getsAuthListener() {
        return FirebaseAuthHandler.sAuthListener;
    }
}
