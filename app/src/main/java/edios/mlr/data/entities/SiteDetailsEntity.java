package edios.mlr.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SiteDetailsEntity implements Parcelable {
    @SerializedName("siteDetailId")
    private Long siteDetailId;
    @SerializedName("orderPrefix")
    private String orderPrefix;
    @SerializedName("orderReadyTime")
    private Long orderReadyTime;
    @SerializedName("siteCurrency")
    private String siteCurrency;
    @SerializedName("timeZone")
    private String timeZone;
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
    @SerializedName("openingDaysTimes")
    private String openingDaysTimes;
    @SerializedName("closingDates")
    private String closingDates;
    @SerializedName("euAppShutdown")
    private String euAppShutdown;
    @SerializedName("euAppShutdownMsg")
    private String euAppShutdownMsg;
    @SerializedName("orderDeliveryType")
    private String orderDeliveryType;
    @SerializedName("deliveryChargesType")
    private String deliveryChargesType;
    @SerializedName("deliveryCharges")
    private Float deliveryCharges;
    @SerializedName("deliveryRadius")
    private Long deliveryRadius;
    @SerializedName("takeAwayRestaurantTime")
    private String takeAwayRestaurantTime;
    @SerializedName("deliveryRestaurantTime")
    private String deliveryRestaurantTime;
    @SerializedName("orderDeliveryTime")
    private Long orderDeliveryTime;
    @SerializedName("deliveryType")
    private String deliveryType;
    @SerializedName("siteId")
    private Long siteId;
    @SerializedName("accountId")
    private Long accountId;

    @Override
    public String toString() {
        return "SiteDetailsEntity{" +
                "siteDetailId=" + siteDetailId +
                ", orderPrefix='" + orderPrefix + '\'' +
                ", orderReadyTime=" + orderReadyTime +
                ", siteCurrency='" + siteCurrency + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", itemWiseSpiceLevels='" + itemWiseSpiceLevels + '\'' +
                ", orderCancellationInMin='" + orderCancellationInMin + '\'' +
                ", adminAppVersion=" + adminAppVersion +
                ", adminAppForceUpgrade='" + adminAppForceUpgrade + '\'' +
                ", euAppVersion=" + euAppVersion +
                ", euAppForceUpgrade='" + euAppForceUpgrade + '\'' +
                ", openingDaysTimes='" + openingDaysTimes + '\'' +
                ", closingDates='" + closingDates + '\'' +
                ", euAppShutdown='" + euAppShutdown + '\'' +
                ", euAppShutdownMsg='" + euAppShutdownMsg + '\'' +
                ", orderDeliveryType='" + orderDeliveryType + '\'' +
                ", deliveryChargesType='" + deliveryChargesType + '\'' +
                ", deliveryCharges=" + deliveryCharges +
                ", deliveryRadius=" + deliveryRadius +
                ", takeAwayRestaurantTime='" + takeAwayRestaurantTime + '\'' +
                ", deliveryRestaurantTime='" + deliveryRestaurantTime + '\'' +
                ", orderDeliveryTime=" + orderDeliveryTime +
                ", deliveryType='" + deliveryType + '\'' +
                ", siteId=" + siteId +
                ", accountId=" + accountId +
                '}';
    }

    public SiteDetailsEntity() {
    }

    protected SiteDetailsEntity(Parcel in) {
        if (in.readByte() == 0) {
            siteDetailId = null;
        } else {
            siteDetailId = in.readLong();
        }
        orderPrefix = in.readString();
        if (in.readByte() == 0) {
            orderReadyTime = null;
        } else {
            orderReadyTime = in.readLong();
        }
        siteCurrency = in.readString();
        timeZone = in.readString();
        itemWiseSpiceLevels = in.readString();
        orderCancellationInMin = in.readString();
        if (in.readByte() == 0) {
            adminAppVersion = null;
        } else {
            adminAppVersion = in.readFloat();
        }
        adminAppForceUpgrade = in.readString();
        if (in.readByte() == 0) {
            euAppVersion = null;
        } else {
            euAppVersion = in.readFloat();
        }
        euAppForceUpgrade = in.readString();
        openingDaysTimes = in.readString();
        closingDates = in.readString();
        euAppShutdown = in.readString();
        euAppShutdownMsg = in.readString();
        orderDeliveryType = in.readString();
        deliveryChargesType = in.readString();
        if (in.readByte() == 0) {
            deliveryCharges = null;
        } else {
            deliveryCharges = in.readFloat();
        }
        if (in.readByte() == 0) {
            deliveryRadius = null;
        } else {
            deliveryRadius = in.readLong();
        }
        takeAwayRestaurantTime = in.readString();
        deliveryRestaurantTime = in.readString();
        if (in.readByte() == 0) {
            orderDeliveryTime = null;
        } else {
            orderDeliveryTime = in.readLong();
        }
        deliveryType = in.readString();
        if (in.readByte() == 0) {
            siteId = null;
        } else {
            siteId = in.readLong();
        }
        if (in.readByte() == 0) {
            accountId = null;
        } else {
            accountId = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (siteDetailId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(siteDetailId);
        }
        dest.writeString(orderPrefix);
        if (orderReadyTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(orderReadyTime);
        }
        dest.writeString(siteCurrency);
        dest.writeString(timeZone);
        dest.writeString(itemWiseSpiceLevels);
        dest.writeString(orderCancellationInMin);
        if (adminAppVersion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(adminAppVersion);
        }
        dest.writeString(adminAppForceUpgrade);
        if (euAppVersion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(euAppVersion);
        }
        dest.writeString(euAppForceUpgrade);
        dest.writeString(openingDaysTimes);
        dest.writeString(closingDates);
        dest.writeString(euAppShutdown);
        dest.writeString(euAppShutdownMsg);
        dest.writeString(orderDeliveryType);
        dest.writeString(deliveryChargesType);
        if (deliveryCharges == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(deliveryCharges);
        }
        if (deliveryRadius == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deliveryRadius);
        }
        dest.writeString(takeAwayRestaurantTime);
        dest.writeString(deliveryRestaurantTime);
        if (orderDeliveryTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(orderDeliveryTime);
        }
        dest.writeString(deliveryType);
        if (siteId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(siteId);
        }
        if (accountId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(accountId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SiteDetailsEntity> CREATOR = new Creator<SiteDetailsEntity>() {
        @Override
        public SiteDetailsEntity createFromParcel(Parcel in) {
            return new SiteDetailsEntity(in);
        }

        @Override
        public SiteDetailsEntity[] newArray(int size) {
            return new SiteDetailsEntity[size];
        }
    };

    public Long getSiteDetailId() {
        return siteDetailId;
    }

    public void setSiteDetailId(Long siteDetailId) {
        this.siteDetailId = siteDetailId;
    }

    public String getOrderPrefix() {
        return orderPrefix;
    }

    public void setOrderPrefix(String orderPrefix) {
        this.orderPrefix = orderPrefix;
    }

    public Long getOrderReadyTime() {
        return orderReadyTime;
    }

    public void setOrderReadyTime(Long orderReadyTime) {
        this.orderReadyTime = orderReadyTime;
    }

    public String getSiteCurrency() {
        return siteCurrency;
    }

    public void setSiteCurrency(String siteCurrency) {
        this.siteCurrency = siteCurrency;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getItemWiseSpiceLevels() {
        return itemWiseSpiceLevels;
    }

    public void setItemWiseSpiceLevels(String itemWiseSpiceLevels) {
        this.itemWiseSpiceLevels = itemWiseSpiceLevels;
    }

    public String getOrderCancellationInMin() {
        return orderCancellationInMin;
    }

    public void setOrderCancellationInMin(String orderCancellationInMin) {
        this.orderCancellationInMin = orderCancellationInMin;
    }

    public Float getAdminAppVersion() {
        return adminAppVersion;
    }

    public void setAdminAppVersion(Float adminAppVersion) {
        this.adminAppVersion = adminAppVersion;
    }

    public String getAdminAppForceUpgrade() {
        return adminAppForceUpgrade;
    }

    public void setAdminAppForceUpgrade(String adminAppForceUpgrade) {
        this.adminAppForceUpgrade = adminAppForceUpgrade;
    }

    public Float getEuAppVersion() {
        return euAppVersion;
    }

    public void setEuAppVersion(Float euAppVersion) {
        this.euAppVersion = euAppVersion;
    }

    public String getEuAppForceUpgrade() {
        return euAppForceUpgrade;
    }

    public void setEuAppForceUpgrade(String euAppForceUpgrade) {
        this.euAppForceUpgrade = euAppForceUpgrade;
    }

    public String getOpeningDaysTimes() {
        return openingDaysTimes;
    }

    public void setOpeningDaysTimes(String openingDaysTimes) {
        this.openingDaysTimes = openingDaysTimes;
    }

    public String getClosingDates() {
        return closingDates;
    }

    public void setClosingDates(String closingDates) {
        this.closingDates = closingDates;
    }

    public String getEuAppShutdown() {
        return euAppShutdown;
    }

    public void setEuAppShutdown(String euAppShutdown) {
        this.euAppShutdown = euAppShutdown;
    }

    public String getEuAppShutdownMsg() {
        return euAppShutdownMsg;
    }

    public void setEuAppShutdownMsg(String euAppShutdownMsg) {
        this.euAppShutdownMsg = euAppShutdownMsg;
    }

    public String getOrderDeliveryType() {
        return orderDeliveryType;
    }

    public void setOrderDeliveryType(String orderDeliveryType) {
        this.orderDeliveryType = orderDeliveryType;
    }

    public String getDeliveryChargesType() {
        return deliveryChargesType;
    }

    public void setDeliveryChargesType(String deliveryChargesType) {
        this.deliveryChargesType = deliveryChargesType;
    }

    public Float getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Float deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Long getDeliveryRadius() {
        return deliveryRadius;
    }

    public void setDeliveryRadius(Long deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
    }

    public String getTakeAwayRestaurantTime() {
        return takeAwayRestaurantTime;
    }

    public void setTakeAwayRestaurantTime(String takeAwayRestaurantTime) {
        this.takeAwayRestaurantTime = takeAwayRestaurantTime;
    }

    public String getDeliveryRestaurantTime() {
        return deliveryRestaurantTime;
    }

    public void setDeliveryRestaurantTime(String deliveryRestaurantTime) {
        this.deliveryRestaurantTime = deliveryRestaurantTime;
    }

    public Long getOrderDeliveryTime() {
        return orderDeliveryTime;
    }

    public void setOrderDeliveryTime(Long orderDeliveryTime) {
        this.orderDeliveryTime = orderDeliveryTime;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
