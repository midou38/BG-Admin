package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import edios.mlr.data.entities.Restaurant;
import edios.mlr.data.entities.SiteDetailsEntity;


public class GenericResponse implements Parcelable {

    @SerializedName("Result_Status")
    private String resultStatus;
    @SerializedName("Result_Message")
    private String resultMessage;
    @SerializedName("Updated_Date")
    private String updatedDate;
    @SerializedName("Result_Output")
    private String resultOutput;
    @SerializedName("Result_Code")
    private String resultCode;
    @SerializedName("Site_Details")
    private SiteDetailsEntity siteDetails;
    @SerializedName("Restaurant_Info")
    private Restaurant restaurant;

    protected GenericResponse(Parcel in) {
        resultStatus = in.readString();
        resultMessage = in.readString();
        updatedDate = in.readString();
        resultOutput = in.readString();
        resultCode = in.readString();
        siteDetails = in.readParcelable(SiteDetailsEntity.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resultStatus);
        dest.writeString(resultMessage);
        dest.writeString(updatedDate);
        dest.writeString(resultOutput);
        dest.writeString(resultCode);
        dest.writeParcelable(siteDetails, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GenericResponse> CREATOR = new Creator<GenericResponse>() {
        @Override
        public GenericResponse createFromParcel(Parcel in) {
            return new GenericResponse(in);
        }

        @Override
        public GenericResponse[] newArray(int size) {
            return new GenericResponse[size];
        }
    };

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getResultOutput() {
        return resultOutput;
    }

    public void setResultOutput(String resultOutput) {
        this.resultOutput = resultOutput;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public SiteDetailsEntity getSiteDetails() {
        return siteDetails;
    }

    public void setSiteDetails(SiteDetailsEntity siteDetails) {
        this.siteDetails = siteDetails;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "resultStatus='" + resultStatus + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                ", resultOutput='" + resultOutput + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", siteDetails=" + siteDetails +
                ", restaurant=" + restaurant +
                '}';
    }
}