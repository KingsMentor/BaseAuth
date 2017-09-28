package xyz.belvi.baseauth.auth.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;

import xyz.belvi.baseauth.auth.AuthDetailsFragment;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth.R;

/**
 * Created by zone2 on 9/18/17.
 */

public class AuthActivity extends AppCompatActivity {


    public static void startFirebasePhoneAuth(Context context, AuthListeners.FirebaseAuthListener authListener) {
        AuthHandler.init(authListener);
        context.startActivity(new Intent(context, AuthActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth.getInstance();
        setContentView(R.layout.verify_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_content_frame, new AuthDetailsFragment()).commitAllowingStateLoss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0)
            super.onBackPressed();
        else {
            finish();
            AuthHandler.getsAuthListener().authIgnored();
        }
    }
}
