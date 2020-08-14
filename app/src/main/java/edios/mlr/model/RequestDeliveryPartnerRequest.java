package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RequestDeliveryPartnerRequest implements Parcelable {

    @SerializedName("signatureKey")
    private String signatureKey;
    @SerializedName("userName")
    private String userName;
    @SerializedName("appEnd")
    private String appEnd;
    @SerializedName("deliveryPartnerId")
    private Long deliveryPartnerId;
    @SerializedName("accountId")
    private Long accountId;
    @SerializedName("siteId")
    private Long siteId;
    @SerializedName("orderNumber")
    private String orderNumber;
    @SerializedName("userFcmId")
    private String userFcmId;
    @SerializedName("partnerName")
    private String partnerName;
    @SerializedName("password")
    private String password;

    public RequestDeliveryPartnerRequest() {
    }

    protected RequestDeliveryPartnerRequest(Parcel in) {
        signatureKey = in.readString();
        userName = in.readString();
        appEnd = in.readString();
        if (in.readByte() == 0) {
            deliveryPartnerId = null;
        } else {
            deliveryPartnerId = in.readLong();
        }
        if (in.readByte() == 0) {
            accountId = null;
        } else {
            accountId = in.readLong();
        }
        if (in.readByte() == 0) {
            siteId = null;
        } else {
            siteId = in.readLong();
        }
        orderNumber = in.readString();
        userFcmId = in.readString();
        partnerName = in.readString();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(signatureKey);
        dest.writeString(userName);
        dest.writeString(appEnd);
        if (deliveryPartnerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deliveryPartnerId);
        }
        if (accountId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(accountId);
        }
        if (siteId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(siteId);
        }
        dest.writeString(orderNumber);
        dest.writeString(userFcmId);
        dest.writeString(partnerName);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RequestDeliveryPartnerRequest> CREATOR = new Creator<RequestDeliveryPartnerRequest>() {
        @Override
        public RequestDeliveryPartnerRequest createFromParcel(Parcel in) {
            return new RequestDeliveryPartnerRequest(in);
        }

        @Override
        public RequestDeliveryPartnerRequest[] newArray(int size) {
            return new RequestDeliveryPartnerRequest[size];
        }
    };

    @Override
    public String toString() {
        return "RequestDeliveryPartnerRequest{" +
                "signatureKey='" + signatureKey + '\'' +
                ", userName='" + userName + '\'' +
                ", appEnd='" + appEnd + '\'' +
                ", deliveryPartnerId=" + deliveryPartnerId +
                ", accountId=" + accountId +
                ", siteId=" + siteId +
                ", orderNumber='" + orderNumber + '\'' +
                ", userFcmId='" + userFcmId + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppEnd() {
        return appEnd;
    }

    public void setAppEnd(String appEnd) {
        this.appEnd = appEnd;
    }

    public Long getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(Long deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserFcmId() {
        return userFcmId;
    }

    public void setUserFcmId(String userFcmId) {
        this.userFcmId = userFcmId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
