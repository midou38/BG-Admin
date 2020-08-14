package edios.mlr.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Restaurant implements Parcelable {
    @SerializedName("siteId")
    private Long siteId;
    @SerializedName("accountId")
    private Long accountId;
    @SerializedName("address1")
    private String address1;
    @SerializedName("address2")
    private String address2;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("country")
    private String country;
    @SerializedName("zipCode")
    private String zipCode;
    @SerializedName("siteName")
    private String siteName;
    @SerializedName("workPhone")
    private String workPhone;
    @SerializedName("siteStatus")
    private String siteStatus;
    @SerializedName("siteLocation")
    private String siteLocation;
    @SerializedName("siteImageUrl")
    private String siteImageUrl;

    @Override
    public String toString() {
        return "Restaurant{" +
                "siteId=" + siteId +
                ", accountId=" + accountId +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", siteName='" + siteName + '\'' +
                ", workPhone='" + workPhone + '\'' +
                ", siteStatus='" + siteStatus + '\'' +
                ", siteLocation='" + siteLocation + '\'' +
                ", siteImageUrl='" + siteImageUrl + '\'' +
                '}';
    }

    protected Restaurant(Parcel in) {
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
        address1 = in.readString();
        address2 = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        zipCode = in.readString();
        siteName = in.readString();
        workPhone = in.readString();
        siteStatus = in.readString();
        siteLocation = in.readString();
        siteImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(zipCode);
        dest.writeString(siteName);
        dest.writeString(workPhone);
        dest.writeString(siteStatus);
        dest.writeString(siteLocation);
        dest.writeString(siteImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getSiteStatus() {
        return siteStatus;
    }

    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getSiteImageUrl() {
        return siteImageUrl;
    }

    public void setSiteImageUrl(String siteImageUrl) {
        this.siteImageUrl = siteImageUrl;
    }
}
