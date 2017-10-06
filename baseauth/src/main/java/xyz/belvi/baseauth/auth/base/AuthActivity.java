package xyz.belvi.baseauth.auth.base;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;
import xyz.belvi.baseauth.auth.AuthDetailsFragment;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth.R;

/**
 * Created by zone2 on 9/18/17.
 */

abstract class AuthActivity extends AppCompatActivity {


    protected static final String STYLE_KEY = " xyz.belvi.baseauth.auth.base.STYLE_KEY";
    private boolean showHelp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    protected abstract void bindAuthResult(AuthListeners.AuthResults authResults);

    protected abstract void manualAuth(String code);

    protected abstract void authPhone(PhoneNumberVerifier.Countries selectedCountry, String phoneNumber, boolean forceResendingToken);


}
