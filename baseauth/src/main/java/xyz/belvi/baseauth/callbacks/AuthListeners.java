package xyz.belvi.baseauth.callbacks;

import android.app.Activity;



/**
 * Created by zone2 on 9/21/17.
 */

public interface AuthListeners {

    void authIgnored();

    void helpClicked(Activity activity);

    interface Auths {
        abstract void codeSent();

        abstract void timeOut();

        abstract void verificationFailure(Exception e);
    }


}
