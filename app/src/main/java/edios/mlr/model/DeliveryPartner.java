package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DeliveryPartner implements Parcelable {

    @SerializedName("accountID")
    private Long accountID;
    @SerializedName("siteID")
    private Long siteID;
    @SerializedName("deliveryPartnerID")
    private Long deliveryPartnerID;
    @SerializedName("createdDate")
    private Date createdDate;
    @SerializedName("lastLoginDate")
    private Date lastLoginDate;
    @SerializedName("userName")
    private String userName;
    @SerializedName("partnerName")
    private String partnerName;
    @SerializedName("password")
    private String password;
    @SerializedName("userAddress")
    private String userAddress;
    @SerializedName("userEmailAddress")
    private String
            userEmailAddress;
    @SerializedName("userFcmId")
    private String userFcmId;
    @SerializedName("userImageUrl")
    private String userImageUrl;
    @SerializedName("userMobile")
    private String userMobile;
    @SerializedName("lastUpdatedDate")
    private String
            lastUpdatedDate;
    @SerializedName("userStatus")
    private String userStatus;
    @SerializedName("userType")
    private String userType;
    @SerializedName("partnerAvailable")
    private String partnerAvailable;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("userLoginType")
    private String userLoginType;

    public DeliveryPartner() {
    }

    protected DeliveryPartner(Parcel in) {
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
        if (in.readByte() == 0) {
            deliveryPartnerID = null;
        } else {
            deliveryPartnerID = in.readLong();
        }
        userName = in.readString();
        partnerName = in.readString();
        password = in.readString();
        userAddress = in.readString();
        userEmailAddress = in.readString();
        userFcmId = in.readString();
        userImageUrl = in.readString();
        userMobile = in.readString();
        lastUpdatedDate = in.readString();
        userStatus = in.readString();
        userType = in.readString();
        partnerAvailable = in.readString();
        deviceId = in.readString();
        userLoginType = in.readString();
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
        if (deliveryPartnerID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deliveryPartnerID);
        }
        dest.writeString(userName);
        dest.writeString(partnerName);
        dest.writeString(password);
        dest.writeString(userAddress);
        dest.writeString(userEmailAddress);
        dest.writeString(userFcmId);
        dest.writeString(userImageUrl);
        dest.writeString(userMobile);
        dest.writeString(lastUpdatedDate);
        dest.writeString(userStatus);
        dest.writeString(userType);
        dest.writeString(partnerAvailable);
        dest.writeString(deviceId);
        dest.writeString(userLoginType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryPartner> CREATOR = new Creator<DeliveryPartner>() {
        @Override
        public DeliveryPartner createFromParcel(Parcel in) {
            return new DeliveryPartner(in);
        }

        @Override
        public DeliveryPartner[] newArray(int size) {
            return new DeliveryPartner[size];
        }
    };

    @Override
    public String toString() {
        return "DeliveryPartner{" +
                "accountID=" + accountID +
                ", siteID=" + siteID +
                ", deliveryPartnerID=" + deliveryPartnerID +
                ", createdDate=" + createdDate +
                ", lastLoginDate=" + lastLoginDate +
                ", userName='" + userName + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", password='" + password + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userEmailAddress='" + userEmailAddress + '\'' +
                ", userFcmId='" + userFcmId + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", lastUpdatedDate='" + lastUpdatedDate + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", userType='" + userType + '\'' +
                ", partnerAvailable='" + partnerAvailable + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", userLoginType='" + userLoginType + '\'' +
                '}';
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public Long getDeliveryPartnerID() {
        return deliveryPartnerID;
    }

    public void setDeliveryPartnerID(Long deliveryPartnerID) {
        this.deliveryPartnerID = deliveryPartnerID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getUserFcmId() {
        return userFcmId;
    }

    public void setUserFcmId(String userFcmId) {
        this.userFcmId = userFcmId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPartnerAvailable() {
        return partnerAvailable;
    }

    public void setPartnerAvailable(String partnerAvailable) {
        this.partnerAvailable = partnerAvailable;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserLoginType() {
        return userLoginType;
    }

    public void setUserLoginType(String userLoginType) {
        this.userLoginType = userLoginType;
    }
}
