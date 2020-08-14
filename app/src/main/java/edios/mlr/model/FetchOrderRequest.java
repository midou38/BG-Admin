package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FetchOrderRequest implements Parcelable {

    @SerializedName("accountID")
    private Long accountID;
    @SerializedName("siteID")
    private Long siteID;
    @SerializedName("lastUpdatedDate")
    private String lastUpdatedDate;

    @SerializedName("signatureKey")
    private String signatureKey;

    @SerializedName("customerUserPK")
    private String customerUserPK;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userEnd")
    private String userEnd;

    public FetchOrderRequest() {
    }

    protected FetchOrderRequest(Parcel in) {
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
        lastUpdatedDate = in.readString();
        signatureKey = in.readString();
        customerUserPK = in.readString();
        userName = in.readString();
        userEnd = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
        dest.writeString(lastUpdatedDate);
        dest.writeString(signatureKey);
        dest.writeString(customerUserPK);
        dest.writeString(userName);
        dest.writeString(userEnd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FetchOrderRequest> CREATOR = new Creator<FetchOrderRequest>() {
        @Override
        public FetchOrderRequest createFromParcel(Parcel in) {
            return new FetchOrderRequest(in);
        }

        @Override
        public FetchOrderRequest[] newArray(int size) {
            return new FetchOrderRequest[size];
        }
    };

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getUserEnd() {
        return userEnd;
    }

    public void setUserEnd(String userEnd) {
        this.userEnd = userEnd;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setCustomerUserPK(String customerUserPK) {
        this.customerUserPK = customerUserPK;
    }

    public String getCustomerUserPK() {
        return customerUserPK;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "FetchOrderRequest{" +
                "accountID=" + accountID +
                ", siteID=" + siteID +
                ", lastUpdatedDate='" + lastUpdatedDate + '\'' +
                ", signatureKey='" + signatureKey + '\'' +
                ", customerUserPK='" + customerUserPK + '\'' +
                ", userName='" + userName + '\'' +
                ", userEnd='" + userEnd + '\'' +
                '}';
    }
}