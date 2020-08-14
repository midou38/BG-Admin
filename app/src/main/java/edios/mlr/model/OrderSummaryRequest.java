package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class OrderSummaryRequest implements Parcelable {
    @SerializedName("accountID")
    private Long accountID;
    @SerializedName("siteID")
    private Long siteID;
    @SerializedName("fromDate")
    private String fromDate;

    @SerializedName("signatureKey")
    private String signatureKey;

    @SerializedName("toDate")
    private String toDate;

    @SerializedName("userName")
    private String userName;

    public OrderSummaryRequest() {
    }

    protected OrderSummaryRequest(Parcel in) {
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
        fromDate = in.readString();
        signatureKey = in.readString();
        toDate = in.readString();
        userName = in.readString();
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
        dest.writeString(fromDate);
        dest.writeString(signatureKey);
        dest.writeString(toDate);
        dest.writeString(userName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderSummaryRequest> CREATOR = new Creator<OrderSummaryRequest>() {
        @Override
        public OrderSummaryRequest createFromParcel(Parcel in) {
            return new OrderSummaryRequest(in);
        }

        @Override
        public OrderSummaryRequest[] newArray(int size) {
            return new OrderSummaryRequest[size];
        }
    };

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "OrderSummaryRequest{" +
                "accountID=" + accountID +
                ", siteID=" + siteID +
                ", fromDate='" + fromDate + '\'' +
                ", signatureKey='" + signatureKey + '\'' +
                ", toDate='" + toDate + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}