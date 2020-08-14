package edios.mlr.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class LoginRequest implements Parcelable {
	@SerializedName("userPassword")
	private String userPassword;
	@SerializedName("signatureKey")
	private String signatureKey;
	@SerializedName("userName")
	private String userName;
	@SerializedName("accountID")
	private Long accountID;
	@SerializedName("siteID")
	private Long siteID;

	public LoginRequest() {
	}

	protected LoginRequest(Parcel in) {
		userPassword = in.readString();
		signatureKey = in.readString();
		userName = in.readString();
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
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userPassword);
		dest.writeString(signatureKey);
		dest.writeString(userName);
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
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<LoginRequest> CREATOR = new Creator<LoginRequest>() {
		@Override
		public LoginRequest createFromParcel(Parcel in) {
			return new LoginRequest(in);
		}

		@Override
		public LoginRequest[] newArray(int size) {
			return new LoginRequest[size];
		}
	};

	public void setAccountID(Long accountID) {
		this.accountID = accountID;
	}

	public void setSiteID(Long siteID) {
		this.siteID = siteID;
	}

	public void setUserPassword(String userPassword){
		this.userPassword = userPassword;
	}

	public String getUserPassword(){
		return userPassword;
	}

	public void setSignatureKey(String signatureKey){
		this.signatureKey = signatureKey;
	}

	public String getSignatureKey(){
		return signatureKey;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	@Override
 	public String toString(){
		return 
			"LoginRequest{" + 
			"userPassword = '" + userPassword + '\'' + 
			",signatureKey = '" + signatureKey + '\'' + 
			",userName = '" + userName + '\'' + 
			"}";
		}
}