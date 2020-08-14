package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
public class CustomerOrderItems implements Parcelable{

	@SerializedName("itemID")
	private int itemID;
	@SerializedName("itemName")
	private String itemName;
	@SerializedName("itemDiscountAmount")
	private double itemDiscountAmount;
	@SerializedName("itemQuantity")
	private int itemQuantity;
	@SerializedName("itemTotalAmount")
	private double itemTotalAmount;
	@SerializedName("orderItemID")
	private int orderItemID;
	@SerializedName("customerOrderID")
	private int customerOrderID;
	@SerializedName("itemPrice")
	private double itemPrice;
	@SerializedName("itemTaxAmount")
	private double itemTaxAmount;
	@SerializedName("itemSpicyLevel")
	private String itemSpicyLevel;
	@SerializedName("itemInstructions")
	private String itemInstructions;

	protected CustomerOrderItems(Parcel in) {
		itemID = in.readInt();
		itemName = in.readString();
		itemDiscountAmount = in.readDouble();
		itemQuantity = in.readInt();
		itemTotalAmount = in.readDouble();
		orderItemID = in.readInt();
		customerOrderID = in.readInt();
		itemPrice = in.readDouble();
		itemTaxAmount = in.readDouble();
		itemSpicyLevel = in.readString();
		itemInstructions = in.readString();
	}

	public static final Creator<CustomerOrderItems> CREATOR = new Creator<CustomerOrderItems>() {
		@Override
		public CustomerOrderItems createFromParcel(Parcel in) {
			return new CustomerOrderItems(in);
		}

		@Override
		public CustomerOrderItems[] newArray(int size) {
			return new CustomerOrderItems[size];
		}
	};

	public String getItemInstructions() {
		return itemInstructions;
	}

	public int getItemID(){
		return itemID;
	}

	public String getItemName(){
		return itemName;
	}

	public double getItemDiscountAmount(){
		return itemDiscountAmount;
	}

	public int getItemQuantity(){
		return itemQuantity;
	}

	public double getItemTotalAmount(){
		return itemTotalAmount;
	}

	public int getOrderItemID(){
		return orderItemID;
	}

	public int getCustomerOrderID(){
		return customerOrderID;
	}

	public double getItemPrice(){
		return itemPrice;
	}

	public double getItemTaxAmount(){
		return itemTaxAmount;
	}

	public String getItemSpicyLevel(){
		return itemSpicyLevel;
	}

	@Override
 	public String toString(){
		return 
			"PreviousOrderItemsItem{" + 
			"itemID = '" + itemID + '\'' + 
			",itemName = '" + itemName + '\'' + 
			",itemDiscountAmount = '" + itemDiscountAmount + '\'' + 
			",itemQuantity = '" + itemQuantity + '\'' + 
			",itemTotalAmount = '" + itemTotalAmount + '\'' + 
			",orderItemID = '" + orderItemID + '\'' + 
			",customerOrderID = '" + customerOrderID + '\'' + 
			",itemPrice = '" + itemPrice + '\'' + 
			",itemTaxAmount = '" + itemTaxAmount + '\'' + 
			",itemSpicyLevel = '" + itemSpicyLevel + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setItemDiscountAmount(double itemDiscountAmount) {
		this.itemDiscountAmount = itemDiscountAmount;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public void setItemTotalAmount(double itemTotalAmount) {
		this.itemTotalAmount = itemTotalAmount;
	}

	public void setOrderItemID(int orderItemID) {
		this.orderItemID = orderItemID;
	}

	public void setCustomerOrderID(int customerOrderID) {
		this.customerOrderID = customerOrderID;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public void setItemTaxAmount(double itemTaxAmount) {
		this.itemTaxAmount = itemTaxAmount;
	}

	public void setItemSpicyLevel(String itemSpicyLevel) {
		this.itemSpicyLevel = itemSpicyLevel;
	}

	public void setItemInstructions(String itemInstructions) {
		this.itemInstructions = itemInstructions;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(itemID);
		parcel.writeString(itemName);
		parcel.writeDouble(itemDiscountAmount);
		parcel.writeInt(itemQuantity);
		parcel.writeDouble(itemTotalAmount);
		parcel.writeInt(orderItemID);
		parcel.writeInt(customerOrderID);
		parcel.writeDouble(itemPrice);
		parcel.writeDouble(itemTaxAmount);
		parcel.writeString(itemSpicyLevel);
		parcel.writeString(itemInstructions);
	}
}