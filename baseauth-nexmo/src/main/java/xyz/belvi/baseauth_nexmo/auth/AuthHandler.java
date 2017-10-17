package xyz.belvi.baseauth_nexmo.auth;


import xyz.belvi.baseauth_nexmo.callbacks.NexmoAuthListener;

/**
 * Created by zone2 on 9/21/17.
 */

class AuthHandler {

    private static NexmoAuthListener sNexmoAuthListener;

    public static void init(NexmoAuthListener authListener) {
        if (sNexmoAuthListener == null)
            sNexmoAuthListener = authListener;
    }

    public static NexmoAuthListener getsAuthListener() {
        return sNexmoAuthListener;
    }
}
