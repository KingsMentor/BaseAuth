package xyz.belvi.baseauthsample;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthCredential;

//import xyz.belvi.baseauth_firebase.auth.FireAuthActivity;
//import xyz.belvi.baseauth_firebase.callbacks.FirebaseAuthListener;
import xyz.belvi.baseauth_sinch.auth.SinchAuthActivity;
import xyz.belvi.baseauth_sinch.callbacks.SinchAuthListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SinchAuthActivity.startSinchAuth(this, new SinchAuthListener() {
            public void onAuthCompleted(String phoneNumber) {

            }

            public void authIgnored() {

            }

            public void helpClicked(Context activity) {

            }
        }, "d99edfa6-4467-41f0-b498-f70f03331884", R.style.BaseAuthStyle);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
