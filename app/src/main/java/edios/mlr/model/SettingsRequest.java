package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SettingsRequest implements Parcelable {
    @SerializedName("signatureKey")
    private String signatureKey;
    @SerializedName("userName")
    private String userName;
    @SerializedName("readyTimeInMin")
    private String readyTimeInMin;
    @SerializedName("accountID")
    private Long accountID;
    @SerializedName("siteID")
    private Long siteID;

    public SettingsRequest() {
    }

    protected SettingsRequest(Parcel in) {
        signatureKey = in.readString();
        userName = in.readString();
        readyTimeInMin = in.readString();
        if (in.readByte() == 0) {
            accountID = null;
        } else {
            accountID = in.readLong();
        }
        if (in.readByte() == 0) {
            siteID = null;
        } else {
            siteID = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(signatureKey);
        dest.writeString(userName);
        dest.writeString(readyTimeInMin);
        if (accountID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(accountID);
        }
        if (siteID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(siteID);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SettingsRequest> CREATOR = new Creator<SettingsRequest>() {
        @Override
        public SettingsRequest createFromParcel(Parcel in) {
            return new SettingsRequest(in);
        }

        @Override
        public SettingsRequest[] newArray(int size) {
            return new SettingsRequest[size];
        }
    };

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }


    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setReadyTimeInMin(String readyTimeInMin) {
        this.readyTimeInMin = readyTimeInMin;
    }   
}
