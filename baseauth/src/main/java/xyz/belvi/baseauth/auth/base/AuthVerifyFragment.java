package xyz.belvi.baseauth.auth.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


import com.belvi.validator.PhoneFormatException;
import com.belvi.validator.PhoneNumberValidator;

import xyz.belvi.baseauth.R;
import xyz.belvi.baseauth.callbacks.AuthListeners;
import xyz.belvi.baseauth.custom.URLSpanNoUnderline;

/**
 * Created by zone2 on 9/19/17.
 */

public class AuthVerifyFragment extends Fragment implements AuthListeners.AuthResults {


    private final String PHONE_KEY = "PHONE_KEY";
    private final String COUNTRY_KEY = "COUNTRY_KEY";
    private final String CODE_SIZE = "CODE_SIZE";


    int secCounter;
    Runnable runnable;
    Handler handler = new Handler();

    public AuthVerifyFragment startFragment(String phone, String country, int codeSize) {
        Bundle bundle = new Bundle();
        bundle.putString(PHONE_KEY, phone);
        bundle.putString(COUNTRY_KEY, country);
        bundle.putInt(CODE_SIZE, codeSize);
        setArguments(bundle);
        return this;
    }

    private AppCompatTextView waitField, statusField;
    private View rootView;

    private AuthActivity getAuthActivity() {
        return (AuthActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getAuthActivity().bindAuthResult(this);
        rootView = inflater.inflate(R.layout.auth_verify_fragment, container, false);
        waitField = (AppCompatTextView) rootView.findViewById(R.id.wait_field);
        statusField = (AppCompatTextView) rootView.findViewById(R.id.incorrect_code);
        waitField.setLinksClickable(true);
        waitField.setMovementMethod(LinkMovementMethod.getInstance());
        addCodeFields(getArguments().getInt(CODE_SIZE));
        PhoneNumberValidator.Country country = PhoneNumberValidator.Country.valueOf(getArguments().getString(COUNTRY_KEY));
        String phone = getArguments().getString(PHONE_KEY);
        try {
            ((AppCompatTextView) rootView.findViewById(R.id.phone_instruction)).setText(String.format(getString(R.string.type_in), country.toCountryCode(phone)));
            getAuthActivity().authPhone(country, phone, false);
        } catch (PhoneFormatException e) {
            e.printStackTrace();
        }


        return rootView;
    }

    private void addCodeFields(int codeSize) {
        LinearLayout codeFieldLayout = rootView.findViewById(R.id.code_layout_grp);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int x = 0; x < codeSize; x++) {
            codeFieldLayout.addView(inflater.inflate(R.layout.code_field, codeFieldLayout, false));
        }
        addEvents(codeFieldLayout);
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
                manualAuth();
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


    private void manualAuth() {
        ViewGroup viewGroup = (LinearLayout) rootView.findViewById(R.id.code_layout_grp);
        if (getCode(viewGroup).length() == getArguments().getInt(CODE_SIZE)) {
            getAuthActivity().manualAuth(getCode(viewGroup));
        }
    }


    public void codeSent() {
        manualAuth();
        secCounter = 60;
        runnable = new Runnable() {
            @Override
            public void run() {
                secCounter--;
                if (secCounter != 0) {
                    String dec = "Please wait <a href=''>" + ((secCounter < 10) ? ("0" + secCounter) : secCounter) + " secs</a>";
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

    public void timeOut() {
        handler.removeCallbacks(runnable);
        initResend();
    }


    public void verificationFailure(Exception e) {
        statusField.setText(e.getMessage());
        statusField.setVisibility(View.VISIBLE);
    }


    private void initResend() {
        String dec = "";
        if (getAuthActivity().getAuthMode().isCallSupported()) {
            dec = "<a href='code'><b>Resend Code<b></a> &nbsp;&nbsp;<b>.</b>&nbsp;&nbsp; <a href='call'><b>Call Me<b> . </a>";
        } else {
            dec = "<a href='code'><b>Resend Code<b></a>";
        }
        CharSequence sequence = Html.fromHtml(dec);
        waitField.setText(sequence);
        stripUnderlines(waitField, true);
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
                        statusField.setVisibility(View.GONE);
                        if (url.equalsIgnoreCase("call")) {

                            getAuthActivity().authPhone(PhoneNumberValidator.Country.valueOf(getArguments().getString(COUNTRY_KEY)), getArguments().getString(PHONE_KEY), true);
                        } else {
                        }
//                        authPhone();
                    }
                }
            });
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }


    private void showKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }
}

