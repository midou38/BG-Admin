package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest implements Parcelable {

    @SerializedName("userName")
    private String userName;

    @SerializedName("loginName")
    private String loginName;

    @SerializedName("password")
    private String password;

    @SerializedName("newPassword")
    private String newPassword;

    @SerializedName("signatureKey")
    private String signatureKey;
    @SerializedName("accountID")
    private Long accountID;
    @SerializedName("siteID")
    private Long siteID;

    public ChangePasswordRequest() {
    }

    protected ChangePasswordRequest(Parcel in) {
        userName = in.readString();
        loginName = in.readString();
        password = in.readString();
        newPassword = in.readString();
        signatureKey = in.readString();
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
        dest.writeString(userName);
        dest.writeString(loginName);
        dest.writeString(password);
        dest.writeString(newPassword);
        dest.writeString(signatureKey);
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

    public static final Creator<ChangePasswordRequest> CREATOR = new Creator<ChangePasswordRequest>() {
        @Override
        public ChangePasswordRequest createFromParcel(Parcel in) {
            return new ChangePasswordRequest(in);
        }

        @Override
        public ChangePasswordRequest[] newArray(int size) {
            return new ChangePasswordRequest[size];
        }
    };

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
