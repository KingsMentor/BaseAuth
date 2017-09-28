package xyz.belvi.baseauth.auth.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import appzonegroup.com.phonenumberverifier.PhoneFormatException;
import appzonegroup.com.phonenumberverifier.PhoneNumberVerifier;
import xyz.belvi.baseauth.R;
import xyz.belvi.baseauth.custom.URLSpanNoUnderline;

/**
 * Created by zone2 on 9/19/17.
 */

public class AuthVerifyFragment extends AuthFragment {


    private final String PHONE_KEY = "PHONE_KEY";
    private final String COUNTRY_KEY = "COUNTRY_KEY";

    public AuthVerifyFragment startFragment(String phone, String country) {
        Bundle bundle = new Bundle();
        bundle.putString(PHONE_KEY, phone);
        bundle.putString(COUNTRY_KEY, country);
        setArguments(bundle);
        return this;
    }

    private AppCompatTextView waitField, statusField;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.auth_verify_fragment, container, false);
        waitField = (AppCompatTextView) rootView.findViewById(R.id.wait_field);
        statusField = (AppCompatTextView) rootView.findViewById(R.id.incorrect_code);
        waitField.setLinksClickable(true);
        waitField.setMovementMethod(LinkMovementMethod.getInstance());
        addEvents(((LinearLayout) rootView.findViewById(R.id.code_layour_grp)));
        ((AppCompatTextView) rootView.findViewById(R.id.phone_instruction)).setText(String.format(getString(R.string.type_in), getArguments().getString(PHONE_KEY)));
        authPhone(PhoneNumberVerifier.Countries.valueOf(getArguments().getString(COUNTRY_KEY)), getArguments().getString(PHONE_KEY));

        return rootView;
    }

    private void addEvents(final ViewGroup viewGroup) {
        for (int x = 0; x < viewGroup.getChildCount(); x++) {
            final AppCompatEditText child = (AppCompatEditText) viewGroup.getChildAt(x);
            if (x == 0) {
                child.requestFocus();
                showKeyboard();
            }
            child.setTag(x);
            final int finalX = x;
            child.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_DEL)) {
                        // Perform action on key press
                        predictNext(child, ' ', finalX, viewGroup, true);
                    } else if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                            (Character.isDigit((char) keyEvent.getUnicodeChar()))) {

                        predictNext(child, (char) keyEvent.getUnicodeChar(), finalX, viewGroup, false);
                    }
                    return false;
                }
            });
            child.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    showKeyboard();
                    return true;
                }
            });
            child.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        requestRightChild(viewGroup);
                    }
                }
            });


        }
    }

    private boolean requestRightChild(ViewGroup viewGroup) {
        for (int index = 0; index < viewGroup.getChildCount(); index++) {
            AppCompatEditText child = (AppCompatEditText) viewGroup.getChildAt(index);
            if (child.getText().toString().isEmpty()) {
                child.requestFocus();
                return true;
            }
        }
        AppCompatEditText child = (AppCompatEditText) viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        child.requestFocus();
        return true;
    }

    private boolean predictNext(AppCompatEditText editText, char c, int index, ViewGroup viewGroup, boolean isDelete) {
        if (isDelete) {
            statusField.setVisibility(View.GONE);
            if (index + 1 == viewGroup.getChildCount() && !(editText.getText().toString().trim().isEmpty())) {
                editText.setText("");
            } else if (index != 0) {
                AppCompatEditText child = (AppCompatEditText) viewGroup.getChildAt(index - 1);
                child.setText("");
                child.requestFocus();

            }
        } else if (editText.getText().toString().trim().isEmpty()) {
            editText.setText(String.valueOf(c).trim());
            if (index + 1 < viewGroup.getChildCount()) {
                AppCompatEditText child = (AppCompatEditText) viewGroup.getChildAt(index + 1);
                child.requestFocus();
            } else {
                if (!verificationId.isEmpty()) {
                    manualAuth();
                }
            }

        }

        return false;
    }

    private String getCode(ViewGroup viewGroup) {
        String code = "";
        for (int x = 0; x < viewGroup.getChildCount(); x++) {
            AppCompatEditText child = (AppCompatEditText) viewGroup.getChildAt(x);
            code += child.getText().toString().trim();
        }
        return code;
    }

    int count;
    Runnable runnable;
    Handler handler = new Handler();
    String verificationId = "";
    PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private void manualAuth() {
        ViewGroup viewGroup = (LinearLayout) rootView.findViewById(R.id.code_layour_grp);
        if (getCode(viewGroup).length() == 6) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, getCode(viewGroup));
            signInWithPhoneAuthCredential(credential);
        }
    }


    @Override
    protected void codeSent(final String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

        this.verificationId = verificationId;
        this.forceResendingToken = forceResendingToken;
        manualAuth();
        count = 60;
        runnable = new Runnable() {
            @Override
            public void run() {
                count--;
                if (count != 0) {
                    String dec = "Please wait <a href=''>" + ((count < 10) ? ("0" + count) : count) + " secs</a>";
                    handler.postDelayed(this, 1000);
                    CharSequence sequence = Html.fromHtml(dec);
                    waitField.setText(sequence);
                    stripUnderlines(waitField, false);
                } else {
                    initResend();
                }
            }
        };

        handler.postDelayed(runnable, 1000);


    }

    private void initResend() {
        String dec = "<a href=''><b>Resend Code<b></a>";
        CharSequence sequence = Html.fromHtml(dec);
        waitField.setText(sequence);
        stripUnderlines(waitField, true);
    }

    @Override
    protected void timeOut() {
        handler.removeCallbacks(runnable);
        initResend();
    }

    @Override
    protected void verificationFailure(Exception e) {
        statusField.setText(e.getMessage());
        statusField.setVisibility(View.VISIBLE);
    }

    @Override
    protected void completed(PhoneAuthCredential phoneAuthCredential) {
        PhoneNumberVerifier.Countries countries = PhoneNumberVerifier.Countries.valueOf(getArguments().getString(COUNTRY_KEY));
        try {
            AuthHandler.getsAuthListener().onAuthCompleted(phoneAuthCredential, countries.ToCountryCode(countries, getArguments().getString(PHONE_KEY)));
        } catch (PhoneFormatException e) {
            e.printStackTrace();
        }
    }


    private void stripUnderlines(TextView textView, final boolean shouldClick) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline("", new URLSpanNoUnderline.OnClickListener() {
                @Override
                public void onClick(String url) {
                    if (shouldClick) {
                        waitField.setText("Please wait.");
                        authPhone(PhoneNumberVerifier.Countries.valueOf(getArguments().getString(COUNTRY_KEY)), getArguments().getString(PHONE_KEY), forceResendingToken);
                        statusField.setVisibility(View.GONE);
//                        authPhone();
                    }
                }
            });
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }
}

