package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrderSummaryItem implements Parcelable {

	@SerializedName("totalAmount")
	private double totalAmount;

	@SerializedName("totalOrders")
	private int totalOrders;

	@SerializedName("orderDate")
	private String orderDate;

	public OrderSummaryItem() {
	}

	protected OrderSummaryItem(Parcel in) {
		totalAmount = in.readDouble();
		totalOrders = in.readInt();
		orderDate = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(totalAmount);
		dest.writeInt(totalOrders);
		dest.writeString(orderDate);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<OrderSummaryItem> CREATOR = new Creator<OrderSummaryItem>() {
		@Override
		public OrderSummaryItem createFromParcel(Parcel in) {
			return new OrderSummaryItem(in);
		}

		@Override
		public OrderSummaryItem[] newArray(int size) {
			return new OrderSummaryItem[size];
		}
	};

	public double getTotalAmount(){
		return totalAmount;
	}

	public int getTotalOrders(){
		return totalOrders;
	}

	public String getOrderDate(){
		return orderDate;
	}

	@Override
 	public String toString(){
		return 
			"OrderSummaryItem{" + 
			"totalAmount = '" + totalAmount + '\'' + 
			",totalOrders = '" + totalOrders + '\'' + 
			",orderDate = '" + orderDate + '\'' + 
			"}";
		}
}