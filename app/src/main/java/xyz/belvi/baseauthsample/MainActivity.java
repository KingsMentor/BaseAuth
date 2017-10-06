package xyz.belvi.baseauthsample;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthCredential;

import xyz.belvi.baseauth_firebase.auth.FireAuthActivity;
import xyz.belvi.baseauth_firebase.callbacks.FirebaseAuthListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireAuthActivity.startFirebasePhoneAuth(this, new FirebaseAuthListener() {
            public void authIgnored() {
                Toast.makeText(MainActivity.this, "auth ignored", Toast.LENGTH_LONG).show();

            }

            public void helpClicked(Activity activity) {
                Toast.makeText(MainActivity.this, "help clicked", Toast.LENGTH_LONG).show();

            }

            public void onAuthCompleted(PhoneAuthCredential credential, String phoneNumber) {
                Toast.makeText(MainActivity.this, "auth completed for" + phoneNumber, Toast.LENGTH_LONG).show();
            }
        }, R.style.BaseAuthStyle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
