package xyz.belvi.baseauth.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import xyz.belvi.baseauth.R;

/**
 * Created by zone2 on 9/18/17.
 */

public class CCHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView countryName, countryCode;

    public CCHolder(View itemView) {
        super(itemView);
        countryCode = (AppCompatTextView)itemView.findViewById(R.id.cc);
        countryName = (AppCompatTextView)itemView.findViewById(R.id.country_name);
    }

    public AppCompatTextView getCountryName() {
        return this.countryName;
    }

    public AppCompatTextView getCountryCode() {
        return this.countryCode;
    }
}
