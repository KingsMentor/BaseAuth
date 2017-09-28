package xyz.belvi.baseauth.custom;

import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

public class URLSpanNoUnderline extends URLSpan {

    private OnClickListener mListener;

    public URLSpanNoUnderline(String url, OnClickListener mListener) {
        super(url);
        this.mListener = mListener;

    }


    @Override
    public void onClick(View widget) {
        super.onClick(widget);
        mListener.onClick("");
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    public interface OnClickListener {
        void onClick(String url);
    }
}