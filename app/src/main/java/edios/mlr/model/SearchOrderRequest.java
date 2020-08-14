package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SearchOrderRequest implements Parcelable {
    @SerializedName("accountID")
    private Long accountID;
    @SerializedName("siteID")
    private Long siteID;
    @SerializedName("signatureKey")
    private String signatureKey;
    @SerializedName("userName")
    private String userName;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("mobileNo")
    private String mobileNo;
    @SerializedName("eMailAddress")
    private String eMailAddress;
    @SerializedName("orderNo")
    private String orderNo;

    public SearchOrderRequest() {
    }

    protected SearchOrderRequest(Parcel in) {
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
        signatureKey = in.readString();
        userName = in.readString();
        customerName = in.readString();
        mobileNo = in.readString();
        eMailAddress = in.readString();
        orderNo = in.readString();
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
        dest.writeString(signatureKey);
        dest.writeString(userName);
        dest.writeString(customerName);
        dest.writeString(mobileNo);
        dest.writeString(eMailAddress);
        dest.writeString(orderNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchOrderRequest> CREATOR = new Creator<SearchOrderRequest>() {
        @Override
        public SearchOrderRequest createFromParcel(Parcel in) {
            return new SearchOrderRequest(in);
        }

        @Override
        public SearchOrderRequest[] newArray(int size) {
            return new SearchOrderRequest[size];
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

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
