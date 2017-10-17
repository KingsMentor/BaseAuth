package xyz.belvi.baseauth.callbacks;

import android.app.Activity;
import android.content.Context;


/**
 * Created by zone2 on 9/21/17.
 */

public interface AuthListeners {


    void authIgnored();

    void helpClicked(Context activity);

    interface AuthResults {
        void codeSent();

        void timeOut();

        void verificationFailure(Exception e);
    }


}
