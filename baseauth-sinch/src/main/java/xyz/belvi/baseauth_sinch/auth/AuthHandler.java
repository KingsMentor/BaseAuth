package xyz.belvi.baseauth_sinch.auth;


import xyz.belvi.baseauth_sinch.callbacks.SinchAuthListener;

/**
 * Created by zone2 on 9/21/17.
 */

class AuthHandler {

    private static SinchAuthListener sAuthListener;

    public static void init(SinchAuthListener authListener) {
        if (sAuthListener == null)
            sAuthListener = authListener;
    }

    public static SinchAuthListener getsAuthListener() {
        return AuthHandler.sAuthListener;
    }
}
