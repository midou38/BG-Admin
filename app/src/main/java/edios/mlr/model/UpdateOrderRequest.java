package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UpdateOrderRequest implements Parcelable {
    @SerializedName("signatureKey")
    private String signatureKey;
    @SerializedName("userName")
    private String userName;
    @SerializedName("appEnd")
    private String appEnd;
    @SerializedName("orderToBeUpdated")
    private CustomerOrders orderToBeUpdated;
    @SerializedName("deliveryPartnerID")
    private Long deliveryPartnerID;

    public UpdateOrderRequest() {
    }

    @Override
    public String toString() {
        return "UpdateOrderRequest{" +
                "signatureKey='" + signatureKey + '\'' +
                ", userName='" + userName + '\'' +
                ", appEnd='" + appEnd + '\'' +
                ", orderToBeUpdated=" + orderToBeUpdated +
                ", deliveryPartnerID=" + deliveryPartnerID +
                '}';
    }

    protected UpdateOrderRequest(Parcel in) {
        signatureKey = in.readString();
        userName = in.readString();
        appEnd = in.readString();
        orderToBeUpdated = in.readParcelable(CustomerOrders.class.getClassLoader());
        if (in.readByte() == 0) {
            deliveryPartnerID = null;
        } else {
            deliveryPartnerID = in.readLong();
        }
    }

    public static final Creator<UpdateOrderRequest> CREATOR = new Creator<UpdateOrderRequest>() {
        @Override
        public UpdateOrderRequest createFromParcel(Parcel in) {
            return new UpdateOrderRequest(in);
        }

        @Override
        public UpdateOrderRequest[] newArray(int size) {
            return new UpdateOrderRequest[size];
        }
    };

    public Long getDeliveryPartnerID() {
        return deliveryPartnerID;
    }

    public void setDeliveryPartnerID(Long deliveryPartnerID) {
        this.deliveryPartnerID = deliveryPartnerID;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAppEnd(String appEnd) {
        this.appEnd = appEnd;
    }

    public void setOrderToBeUpdated(CustomerOrders orderToBeUpdated) {
        this.orderToBeUpdated = orderToBeUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(signatureKey);
        dest.writeString(userName);
        dest.writeString(appEnd);
        dest.writeParcelable(orderToBeUpdated, flags);
        if (deliveryPartnerID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deliveryPartnerID);
        }
    }
}
