package xyz.belvi.baseauth.countrySelector;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belvi.validator.PhoneNumberValidator;

import xyz.belvi.baseauth.R;

/**
 * Created by zone2 on 9/18/17.
 */

abstract public class CountrySelectorAdapter extends RecyclerView.Adapter<CountrySelectorHolder> {
    private PhoneNumberValidator.Country selectedCountries;

    public CountrySelectorAdapter(PhoneNumberValidator.Country countries) {
        this.selectedCountries = countries;

    }

    @Override
    public CountrySelectorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CountrySelectorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cc_selector_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CountrySelectorHolder holder, final int position) {
        holder.getCountryName().setText(PhoneNumberValidator.Country.values()[position].getCountryName());
        holder.getCountryCode().setText("( +" + String.valueOf(PhoneNumberValidator.Country.values()[position].getCountryCode()) + " )");
        if (selectedCountries == PhoneNumberValidator.Country.values()[position]) {
            holder.getCountryName().setTypeface(null, Typeface.BOLD);
            holder.getCountryCode().setTypeface(null, Typeface.BOLD);
        } else {
            holder.getCountryName().setTypeface(Typeface.DEFAULT);
            holder.getCountryCode().setTypeface(Typeface.DEFAULT);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCountrySelected(PhoneNumberValidator.Country.values()[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PhoneNumberValidator.Country.values().length;
    }

    abstract public void onCountrySelected(PhoneNumberValidator.Country countries);
}
