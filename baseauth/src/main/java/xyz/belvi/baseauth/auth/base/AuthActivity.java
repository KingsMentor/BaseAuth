package xyz.belvi.baseauth.auth.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import xyz.belvi.baseauth.auth.AuthDetailsFragment;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth.R;

/**
 * Created by zone2 on 9/18/17.
 */

public class AuthActivity extends AppCompatActivity {


    private static final String STYLE_KEY = " xyz.belvi.baseauth.auth.base.STYLE_KEY";
    private boolean showHelp;

    public static void startFirebasePhoneAuth(Context context, AuthListeners.FirebaseAuthListener authListener, @StyleRes int styleRes) {
        AuthHandler.init(authListener);
        context.startActivity(new Intent(context, AuthActivity.class).putExtra(STYLE_KEY, styleRes));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth.getInstance();
        initStyle(getIntent().getIntExtra(STYLE_KEY, R.style.BaseAuthStyle));
        setContentView(R.layout.verify_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_content_frame, new AuthDetailsFragment()).commitAllowingStateLoss();

    }

    private void initStyle(int style) {
        TypedArray ta = obtainStyledAttributes(style, R.styleable.BaseAuthStyle);
        showHelp = ta.getBoolean(R.styleable.BaseAuthStyle_ba_show_help, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.findItem(R.id.help).setVisible(showHelp);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
