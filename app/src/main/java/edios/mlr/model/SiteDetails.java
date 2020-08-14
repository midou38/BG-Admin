package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class SiteDetails implements Parcelable {


    @SerializedName("orderReadyTime")
    private int orderReadyTime;
    @SerializedName("timeZone")
    private String timeZone;
    @SerializedName("orderPrefix")
    private String orderPrefix;
    @SerializedName("siteCurrency")
    private String siteCurrency;
    @SerializedName("itemWiseSpiceLevels")
    private String itemWiseSpiceLevels;
    @SerializedName("orderCancellationInMin")
    private String orderCancellationInMin;
    @SerializedName("adminAppVersion")
    private Float adminAppVersion;
    @SerializedName("adminAppForceUpgrade")
    private String adminAppForceUpgrade;
    @SerializedName("euAppVersion")
    private Float euAppVersion;
    @SerializedName("euAppForceUpgrade")
    private String euAppForceUpgrade;

    protected SiteDetails(Parcel in) {
        orderReadyTime = in.readInt();
        timeZone = in.readString();
        orderPrefix = in.readString();
        siteCurrency = in.readString();
        itemWiseSpiceLevels = in.readString();
        orderCancellationInMin = in.readString();
        adminAppVersion = in.readFloat();
        adminAppForceUpgrade = in.readString();
        euAppVersion = in.readFloat();
        euAppForceUpgrade = in.readString();
    }


    public static final Creator<SiteDetails> CREATOR = new Creator<SiteDetails>() {
        @Override
        public SiteDetails createFromParcel(Parcel in) {
            return new SiteDetails(in);
        }

        @Override
        public SiteDetails[] newArray(int size) {
            return new SiteDetails[size];
        }
    };

    public int getOrderReadyTime() {
        return orderReadyTime;
    }


    public String getTimeZone() {
        return timeZone;
    }


    public String getOrderPrefix() {
        return orderPrefix;
    }


    public String getSiteCurrency() {
        return siteCurrency;
    }


    public Float getAdminAppVersion() {
        return adminAppVersion;
    }

    public String getAdminAppForceUpgrade() {
        return adminAppForceUpgrade;
    }

    @Override
    public String toString() {
        return "SiteDetails{" + "orderReadyTime=" + orderReadyTime + ", timeZone='" + timeZone + '\'' + ", orderPrefix='" + orderPrefix + '\'' + ", siteCurrency='" + siteCurrency + '\'' + ", itemWiseSpiceLevels='" + itemWiseSpiceLevels + '\'' + ", orderCancellationInMin='" + orderCancellationInMin + '\'' + ", adminAppVersion='" + adminAppVersion + '\'' + ", adminAppForceUpgrade='" + adminAppForceUpgrade + '\'' + ", euAppVersion='" + euAppVersion + '\'' + ", euAppForceUpgrade='" + euAppForceUpgrade + '\'' + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(orderReadyTime);
        parcel.writeString(timeZone);
        parcel.writeString(orderPrefix);
        parcel.writeString(siteCurrency);
        parcel.writeString(itemWiseSpiceLevels);
        parcel.writeString(orderCancellationInMin);
        parcel.writeFloat(adminAppVersion);
        parcel.writeString(adminAppForceUpgrade);
        parcel.writeFloat(euAppVersion);
        parcel.writeString(euAppForceUpgrade);
    }
}