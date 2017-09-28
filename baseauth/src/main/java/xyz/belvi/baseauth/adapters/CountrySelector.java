package xyz.belvi.baseauth.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Arrays;

import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;
import xyz.belvi.baseauth.R;

public class CountrySelector extends AppCompatActivity {

    public static final String SELECTED_COUNTRY = "SELECTED_COUNTRY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccselector);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSelector();
    }

    private void initSelector() {
        RecyclerView recyclerView = findViewById(R.id.cc_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(false);
        PhoneNumberVerifier.Countries countries = null;
        try {
            countries = PhoneNumberVerifier.Countries.valueOf(getIntent().getStringExtra(SELECTED_COUNTRY));
        } catch (NullPointerException ex) {

        }
        if (countries != null)
            recyclerView.scrollToPosition(Arrays.asList(PhoneNumberVerifier.Countries.values()).indexOf(countries));
        recyclerView.setAdapter(new CountryAdapter(countries) {
            @Override
            public void onCountrySelected(PhoneNumberVerifier.Countries countries) {
                setResult(Activity.RESULT_OK, getIntent().putExtra(SELECTED_COUNTRY, countries.name()));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
