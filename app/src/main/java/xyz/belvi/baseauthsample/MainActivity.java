package xyz.belvi.baseauthsample;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

//import com.google.firebase.auth.PhoneAuthCredential;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.UserObject;

import xyz.belvi.baseauth_nexmo.auth.NexmoAuth;
import xyz.belvi.baseauth_nexmo.auth.NexmoAuthActivity;
import xyz.belvi.baseauth_nexmo.callbacks.NexmoAuthListener;
//
//import xyz.belvi.baseauth_firebase.auth.FireAuthActivity;
//import xyz.belvi.baseauth_firebase.callbacks.FirebaseAuthListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testNexmo();
//        testFireBase();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void testNexmo() {
        NexmoAuthActivity.startNexmoAuth(this, new NexmoAuthListener() {
            public void onAuthCompleted(String phoneNumber) {
                Toast.makeText(MainActivity.this, phoneNumber, Toast.LENGTH_LONG).show();
            }

            public void authIgnored() {

            }

            public void helpClicked(Context activity) {

            }
        }, new NexmoAuth("YOUR_ID", "YOUR_SECRET", 4), R.style.BaseAuthStyle);
    }

//    private void testFireBase() {
//        FireAuthActivity.startFirebasePhoneAuth(this, new FirebaseAuthListener() {
//            @Override
//            public void authIgnored() {
//                Toast.makeText(MainActivity.this, "auth ignored", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void helpClicked(Context context) {
//                Toast.makeText(MainActivity.this, "help clicked", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onAuthCompleted(PhoneAuthCredential credential, String phoneNumber) {
//                Toast.makeText(MainActivity.this, "auth completed for" + phoneNumber, Toast.LENGTH_LONG).show();
//            }
//        }, R.style.BaseAuthStyle);
//    }

//    private void testSinch() {
//        SinchAuthActivity.startSinchAuth(this, new SinchAuthListener() {
//            public void onAuthCompleted(String phoneNumber) {
//
//            }
//
//            public void authIgnored() {
//
//            }
//
//            public void helpClicked(Context activity) {
//
//            }
//        }, "d99edfa6-4467-41f0-b498-f70f03331884", R.style.BaseAuthStyle);
//    }
}
