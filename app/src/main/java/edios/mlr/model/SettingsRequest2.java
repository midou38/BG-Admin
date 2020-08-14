package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SettingsRequest2 implements Parcelable {

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
    @SerializedName("euAppShutdown")
    private String euAppShutdown;

    @SerializedName("deliveryChargesType")
    private String deliveryChargesType;

    @SerializedName("deliveryChargesAmount")
    private Float deliveryChargesAmount;

    @SerializedName("deliveryTime")
    private Long deliveryTime;

    @SerializedName("orderType")
    private String orderType;

    @SerializedName("deliveryRadius")
    private Long deliveryRadius;

    public SettingsRequest2() {
    }

    protected SettingsRequest2(Parcel in) {
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
        euAppShutdown = in.readString();
        deliveryChargesType = in.readString();
        if (in.readByte() == 0) {
            deliveryChargesAmount = null;
        } else {
            deliveryChargesAmount = in.readFloat();
        }
        if (in.readByte() == 0) {
            deliveryTime = null;
        } else {
            deliveryTime = in.readLong();
        }
        orderType = in.readString();
        if (in.readByte() == 0) {
            deliveryRadius = null;
        } else {
            deliveryRadius = in.readLong();
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
        dest.writeString(euAppShutdown);
        dest.writeString(deliveryChargesType);
        if (deliveryChargesAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(deliveryChargesAmount);
        }
        if (deliveryTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deliveryTime);
        }
        dest.writeString(orderType);
        if (deliveryRadius == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deliveryRadius);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SettingsRequest2> CREATOR = new Creator<SettingsRequest2>() {
        @Override
        public SettingsRequest2 createFromParcel(Parcel in) {
            return new SettingsRequest2(in);
        }

        @Override
        public SettingsRequest2[] newArray(int size) {
            return new SettingsRequest2[size];
        }
    };

    public String getEuAppShutdown() {
        return euAppShutdown;
    }

    public void setDeliveryTime(Long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public Long getDeliveryPickupTime() {
        return deliveryTime;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDeliveryChargesType() {
        return deliveryChargesType;
    }


    public void setDeliveryChargesType(String deliveryChargesType) {
        this.deliveryChargesType = deliveryChargesType;
    }

    public Float getDeliveryChargesAmount() {
        return deliveryChargesAmount;
    }

    public void setDeliveryChargesAmount(Float deliveryChargesAmount) {
        this.deliveryChargesAmount = deliveryChargesAmount;
    }

    public Long getDeliveryRadius() {
        return deliveryRadius;
    }

    public void setDeliveryRadius(Long deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
    }

    public void setEuAppShutdown(String euAppShutdown) {
        this.euAppShutdown = euAppShutdown;
    }

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

    @Override
    public String toString() {
        return "SettingsRequest{" +
                "signatureKey='" + signatureKey + '\'' +
                ", userName='" + userName + '\'' +
                ", readyTimeInMin='" + readyTimeInMin + '\'' +
                ", accountID=" + accountID +
                ", siteID=" + siteID +
                ", euAppShutdown='" + euAppShutdown + '\'' +
                '}';
    }
}
