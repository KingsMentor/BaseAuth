package xyz.belvi.baseauth_sinch.callbacks;

import xyz.belvi.baseauth.callbacks.AuthListeners;

/**
 * Created by zone2 on 9/21/17.
 */

public interface SinchAuthListener extends AuthListeners {
    int AUTH_CODE_LENGTH = 4;

    void onAuthCompleted(String phoneNumber);
}


