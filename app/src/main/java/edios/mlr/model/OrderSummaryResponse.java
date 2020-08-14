package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import edios.mlr.model.OrderSummaryItem;

public class OrderSummaryResponse implements Parcelable {

	@SerializedName("Result_Status")
	private String resultStatus;

	@SerializedName("Result_Message")
	private String resultMessage;

	@SerializedName("Updated_Date")
	private String updatedDate;

	@SerializedName("Result_Output")
	private List<OrderSummaryItem> resultOutput;

	@SerializedName("Result_Code")
	private String resultCode;

	public OrderSummaryResponse() {
	}

	protected OrderSummaryResponse(Parcel in) {
		resultStatus = in.readString();
		resultMessage = in.readString();
		updatedDate = in.readString();
		resultOutput = in.createTypedArrayList(OrderSummaryItem.CREATOR);
		resultCode = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(resultStatus);
		dest.writeString(resultMessage);
		dest.writeString(updatedDate);
		dest.writeTypedList(resultOutput);
		dest.writeString(resultCode);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<OrderSummaryResponse> CREATOR = new Creator<OrderSummaryResponse>() {
		@Override
		public OrderSummaryResponse createFromParcel(Parcel in) {
			return new OrderSummaryResponse(in);
		}

		@Override
		public OrderSummaryResponse[] newArray(int size) {
			return new OrderSummaryResponse[size];
		}
	};

	public String getResultStatus(){
		return resultStatus;
	}

	public String getResultMessage(){
		return resultMessage;
	}

	public String getUpdatedDate(){
		return updatedDate;
	}

	public Object getResultOutput(){
		return resultOutput;
	}

	public String getResultCode(){
		return resultCode;
	}


	@Override
	public String toString() {
		return "OrderSummaryResponse{" + "resultStatus='" + resultStatus + '\'' + ", resultMessage='" + resultMessage + '\'' + ", updatedDate='" + updatedDate + '\'' + ", resultOutput=" + resultOutput + ", resultCode='" + resultCode + '\'' + '}';
	}
}