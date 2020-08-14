package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import edios.mlr.data.entities.Restaurant;
import edios.mlr.data.entities.SiteDetailsEntity;


public class FetchOrderResponse implements Parcelable {

    @SerializedName("Result_Status")
    private String resultStatus;

    @SerializedName("Result_Message")
    private String resultMessage;

    @SerializedName("Updated_Date")
    private String updatedDate;

    @SerializedName("Result_Output")
    private ResultOutput resultOutput;

    @SerializedName("Result_Code")
    private String resultCode;

    @SerializedName("Site_Details")
    private SiteDetailsEntity siteDetails;

    @SerializedName("Restaurant_Info")
    private Restaurant restaurant;

	protected FetchOrderResponse(Parcel in) {
		resultStatus = in.readString();
		resultMessage = in.readString();
		updatedDate = in.readString();
		resultOutput = in.readParcelable(ResultOutput.class.getClassLoader());
		resultCode = in.readString();
		siteDetails = in.readParcelable(SiteDetailsEntity.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(resultStatus);
		dest.writeString(resultMessage);
		dest.writeString(updatedDate);
		dest.writeParcelable(resultOutput, flags);
		dest.writeString(resultCode);
		dest.writeParcelable(siteDetails, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<FetchOrderResponse> CREATOR = new Creator<FetchOrderResponse>() {
		@Override
		public FetchOrderResponse createFromParcel(Parcel in) {
			return new FetchOrderResponse(in);
		}

		@Override
		public FetchOrderResponse[] newArray(int size) {
			return new FetchOrderResponse[size];
		}
	};

	public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setResultOutput(ResultOutput resultOutput) {
        this.resultOutput = resultOutput;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
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

    public SiteDetailsEntity getSiteDetails() {
        return siteDetails;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public ResultOutput getResultOutput() {
        return resultOutput;
    }

    public String getResultCode() {
        return resultCode;
    }

	@Override
	public String toString() {
		return "FetchOrderResponse{" +
				"resultStatus='" + resultStatus + '\'' +
				", resultMessage='" + resultMessage + '\'' +
				", updatedDate='" + updatedDate + '\'' +
				", resultOutput=" + resultOutput +
				", resultCode='" + resultCode + '\'' +
				", siteDetails=" + siteDetails +
				", restaurant=" + restaurant +
				'}';
	}
}