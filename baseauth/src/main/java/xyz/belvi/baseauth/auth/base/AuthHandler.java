package xyz.belvi.baseauth.auth.base;

import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth.callbacks.AuthListeners.FirebaseAuthListener;

/**
 * Created by zone2 on 9/21/17.
 */

class AuthHandler {

    private static AuthListeners.FirebaseAuthListener sAuthListener;

    public static void init(AuthListeners.FirebaseAuthListener authListener) {
        if (sAuthListener == null)
            sAuthListener = authListener;
    }

    public static FirebaseAuthListener getsAuthListener() {
        return AuthHandler.sAuthListener;
    }
}
