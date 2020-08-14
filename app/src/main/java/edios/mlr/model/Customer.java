package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
public class Customer implements Parcelable{

	@SerializedName("userImageURL")
	private String userImageURL;

	@SerializedName("mobileNo")
	private String mobileNo;

	@SerializedName("userName")
	private String userName;

	protected Customer(Parcel in) {
		userImageURL = in.readString();
		mobileNo = in.readString();
		userName = in.readString();
	}

	public static final Creator<Customer> CREATOR = new Creator<Customer>() {
		@Override
		public Customer createFromParcel(Parcel in) {
			return new Customer(in);
		}

		@Override
		public Customer[] newArray(int size) {
			return new Customer[size];
		}
	};

	public String getUserImageURL(){
		return userImageURL;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public String getUserName(){
		return userName;
	}

	@Override
 	public String toString(){
		return 
			"Customer{" + 
			"userImageURL = '" + userImageURL + '\'' + 
			",mobileNo = '" + mobileNo + '\'' + 
			",userName = '" + userName + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(userImageURL);
		parcel.writeString(mobileNo);
		parcel.writeString(userName);
	}
}