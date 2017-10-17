package xyz.belvi.baseauth_nexmo.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zone2 on 10/16/17.
 */

public class NexmoAuth implements Parcelable {
    private String apiId, sharedSecret;
    private int pinLength;

    public NexmoAuth(String apiId, String sharedSecret, int pinLength) {
        this.apiId = apiId;
        this.sharedSecret = sharedSecret;
        this.pinLength = pinLength;
    }

    protected NexmoAuth(Parcel in) {
        apiId = in.readString();
        sharedSecret = in.readString();
        pinLength = in.readInt();
    }

    public static final Creator<NexmoAuth> CREATOR = new Creator<NexmoAuth>() {
        @Override
        public NexmoAuth createFromParcel(Parcel in) {
            return new NexmoAuth(in);
        }

        @Override
        public NexmoAuth[] newArray(int size) {
            return new NexmoAuth[size];
        }
    };

    public String getApiId() {
        return this.apiId;
    }

    public String getSharedSecret() {
        return this.sharedSecret;
    }

    public int getPinLength() {
        return this.pinLength;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apiId);
        dest.writeString(sharedSecret);
        dest.writeInt(pinLength);
    }
}
