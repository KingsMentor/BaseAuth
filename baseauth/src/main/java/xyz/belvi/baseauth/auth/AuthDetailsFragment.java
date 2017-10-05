package xyz.belvi.baseauth.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import appzonegroup.com.phonenumberverifier.PhoneFormatException;
import appzonegroup.com.phonenumberverifier.PhoneModel;
import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth.countrySelector.CountrySelectorActivity;
import xyz.belvi.baseauth.R;
import xyz.belvi.baseauth.auth.base.AuthVerifyFragment;

/**
 * Created by zone2 on 9/19/17.
 */

public class AuthDetailsFragment extends Fragment {


    private int R_C = 500;
    private AppCompatEditText phoneCompatEditText;
    private AppCompatButton verifyBtn;
    private View rootView;
    PhoneNumberVerifier.Countries selectedCountry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.auth_fragment, container, false);
        FirebaseAuth.getInstance();
        selectedCountry = new PhoneNumberVerifier().getUserCountry(getContext());
        final AppCompatEditText ccCompatEditText = rootView.findViewById(R.id.country_code_selector);
        phoneCompatEditText = rootView.findViewById(R.id.phone_number);
        phoneCompatEditText.setText(getLineNumber());
        verifyBtn = rootView.findViewById(R.id.verify_btn);
        phoneCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                verifyInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ccCompatEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ccCompatEditText.requestFocus();
                return false;
            }
        });
        ccCompatEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(view.getContext(), CountrySelectorActivity.class).putExtra(CountrySelectorActivity.SELECTED_COUNTRY, selectedCountry == null ? null : selectedCountry.name()), R_C);

            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.auth_content_frame, new AuthVerifyFragment()
                                .startFragment(phoneCompatEditText.getText().toString(), selectedCountry.name(), AuthListeners.AUTH_CODE_LENGTH.FIREBASE_CODE_LENGTH))
                        .commitAllowingStateLoss();
            }
        });

        setCountryCodeText();

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == R_C && resultCode == Activity.RESULT_OK) {
            selectedCountry = PhoneNumberVerifier.Countries.valueOf(data.getStringExtra(CountrySelectorActivity.SELECTED_COUNTRY));
            setCountryCodeText();
        }
    }

    private void setCountryCodeText() {
        if (selectedCountry != null) {
            ((TextView) rootView.findViewById(R.id.country_code_selector)).setText(selectedCountry.getCountryName() + " ( +" + selectedCountry.getCountryCode() + " )");
            verifyInput();
        }
    }

    private String getLineNumber() {
        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String mPhoneNumber = tMgr.getLine1Number();
            return mPhoneNumber;
        }
        return "";
    }


    private void verifyInput() {
        if (selectedCountry != null) {
            String phoneNumber = phoneCompatEditText.getText().toString();
            try {
                PhoneModel phoneModel = selectedCountry.isNumberValid(selectedCountry, phoneNumber);
                verifyBtn.setEnabled(phoneModel.isValidPhoneNumber());
            } catch (PhoneFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
