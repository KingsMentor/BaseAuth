package xyz.belvi.baseauth_nexmo.callbacks;

import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.UserObject;

import xyz.belvi.baseauth.callbacks.AuthListeners;

/**
 * Created by zone2 on 9/21/17.
 */

public interface NexmoAuthListener extends AuthListeners {
    int AUTH_CODE_LENGTH = 4;

    void onAuthCompleted(String phoneNumber);
}


