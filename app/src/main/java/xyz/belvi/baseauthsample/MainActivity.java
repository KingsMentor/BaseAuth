package xyz.belvi.baseauthsample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthCredential;

import xyz.belvi.baseauth.AuthListeners;
import xyz.belvi.baseauth.auth.AuthActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthActivity.startFirebasePhoneAuth(this, new AuthListeners.FirebaseAuthListener() {
            @Override
            public void onAuthCompleted(PhoneAuthCredential credential) {
                Toast.makeText(MainActivity.this, "finished", Toast.LENGTH_LONG).show();
            }
        });
    }
}
