package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class CustomerOrders implements Parcelable {

    @SerializedName("orderDateTime")
    private String orderDateTime;
    @SerializedName("orderType")
    private String orderType;
    @SerializedName("orderNumber")
    private String orderNumber;
    @SerializedName("totalPrice")
    private double totalPrice;
    @SerializedName("totalDiscountAmount")
    private double totalDiscountAmount;
    @SerializedName("customerOrderID")
    private int customerOrderID;
    @SerializedName("promoCodeID")
    private int promoCodeID;
    @SerializedName("tableNumber")
    private String tableNumber;
    @SerializedName("orderStatus")
    private String orderStatus;
    @SerializedName("paymentType")
    private String paymentType;
    @SerializedName("totalOrderAmount")
    private double totalOrderAmount;
    @SerializedName("deliveryChargesAmount")
    private double deliveryChargesAmount;
    @SerializedName("orderStatusDateTime")
    private String orderStatusDateTime;
    @SerializedName("customerUserID")
    private int customerUserID;
    @SerializedName("totalTaxAmount")
    private double totalTaxAmount;
    @SerializedName("promoDiscountAmount")
    private double promoDiscountAmount;
    @SerializedName("paymentStatus")
    private String paymentStatus;
    @SerializedName("orderReadyTimeInmin")
    private int orderReadyTimeInmin;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("orderInstructions")
    private String orderInstructions;
    @SerializedName("orderRating")
    private Long orderRating;
    @SerializedName("orderReview")
    private String orderReview;
    @SerializedName("orderPickupDateTime")
    private String orderPickupDateTime;
    @SerializedName("deliveryStatus")
    private String deliveryStatus;
    @SerializedName("deliveryAddress")
    private String deliveryAddress;
    @SerializedName("deliveryStatusDateTime")
    private Date deliveryStatusDateTime;
    @SerializedName("deliveryPartner")
    private DeliveryPartner deliveryPartner;
    @SerializedName("customerOrderItems")
    private List<CustomerOrderItems> customerOrderItems;
    @SerializedName("updateOrder")
    private boolean updateOrder;
    @SerializedName("deliveryStatusNotes")
    private String deliveryStatusNotes;
    @SerializedName("deliveryPartnerID")
    private Long deliveryPartnerID;
    @SerializedName("deliveryRating")
    private Long deliveryRating;
    @SerializedName("deliveryReview")
    private String deliveryReview;

    public CustomerOrders() {
    }

    protected CustomerOrders(Parcel in) {
        orderDateTime = in.readString();
        orderType = in.readString();
        orderNumber = in.readString();
        totalPrice = in.readDouble();
        totalDiscountAmount = in.readDouble();
        customerOrderID = in.readInt();
        promoCodeID = in.readInt();
        tableNumber = in.readString();
        orderStatus = in.readString();
        paymentType = in.readString();
        totalOrderAmount = in.readDouble();
        deliveryChargesAmount = in.readDouble();
        orderStatusDateTime = in.readString();
        customerUserID = in.readInt();
        totalTaxAmount = in.readDouble();
        promoDiscountAmount = in.readDouble();
        paymentStatus = in.readString();
        orderReadyTimeInmin = in.readInt();
        customer = in.readParcelable(Customer.class.getClassLoader());
        orderInstructions = in.readString();
        if (in.readByte() == 0) {
            orderRating = null;
        } else {
            orderRating = in.readLong();
        }
        orderReview = in.readString();
        orderPickupDateTime = in.readString();
        deliveryStatus = in.readString();
        deliveryAddress = in.readString();
        deliveryPartner = in.readParcelable(DeliveryPartner.class.getClassLoader());
        customerOrderItems = in.createTypedArrayList(CustomerOrderItems.CREATOR);
        updateOrder = in.readByte() != 0;
        deliveryStatusNotes = in.readString();
        if (in.readByte() == 0) {
            deliveryPartnerID = null;
        } else {
            deliveryPartnerID = in.readLong();
        }
        if (in.readByte() == 0) {
            deliveryRating = null;
        } else {
            deliveryRating = in.readLong();
        }
        deliveryReview = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderDateTime);
        dest.writeString(orderType);
        dest.writeString(orderNumber);
        dest.writeDouble(totalPrice);
        dest.writeDouble(totalDiscountAmount);
        dest.writeInt(customerOrderID);
        dest.writeInt(promoCodeID);
        dest.writeString(tableNumber);
        dest.writeString(orderStatus);
        dest.writeString(paymentType);
        dest.writeDouble(totalOrderAmount);
        dest.writeDouble(deliveryChargesAmount);
        dest.writeString(orderStatusDateTime);
        dest.writeInt(customerUserID);
        dest.writeDouble(totalTaxAmount);
        dest.writeDouble(promoDiscountAmount);
        dest.writeString(paymentStatus);
        dest.writeInt(orderReadyTimeInmin);
        dest.writeParcelable(customer, flags);
        dest.writeString(orderInstructions);
        if (orderRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(orderRating);
        }
        dest.writeString(orderReview);
        dest.writeString(orderPickupDateTime);
        dest.writeString(deliveryStatus);
        dest.writeString(deliveryAddress);
        dest.writeParcelable(deliveryPartner, flags);
        dest.writeTypedList(customerOrderItems);
        dest.writeByte((byte) (updateOrder ? 1 : 0));
        dest.writeString(deliveryStatusNotes);
        if (deliveryPartnerID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deliveryPartnerID);
        }
        if (deliveryRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deliveryRating);
        }
        dest.writeString(deliveryReview);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomerOrders> CREATOR = new Creator<CustomerOrders>() {
        @Override
        public CustomerOrders createFromParcel(Parcel in) {
            return new CustomerOrders(in);
        }

        @Override
        public CustomerOrders[] newArray(int size) {
            return new CustomerOrders[size];
        }
    };

    @Override
    public String toString() {
        return "CustomerOrders{" +
                "orderDateTime='" + orderDateTime + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", totalPrice=" + totalPrice +
                ", totalDiscountAmount=" + totalDiscountAmount +
                ", customerOrderID=" + customerOrderID +
                ", promoCodeID=" + promoCodeID +
                ", tableNumber='" + tableNumber + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", totalOrderAmount=" + totalOrderAmount +
                ", deliveryChargesAmount=" + deliveryChargesAmount +
                ", orderStatusDateTime='" + orderStatusDateTime + '\'' +
                ", customerUserID=" + customerUserID +
                ", totalTaxAmount=" + totalTaxAmount +
                ", promoDiscountAmount=" + promoDiscountAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", orderReadyTimeInmin=" + orderReadyTimeInmin +
                ", customer=" + customer +
                ", orderInstructions='" + orderInstructions + '\'' +
                ", orderRating=" + orderRating +
                ", orderReview='" + orderReview + '\'' +
                ", orderPickupDateTime='" + orderPickupDateTime + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", deliveryStatusDateTime=" + deliveryStatusDateTime +
                ", deliveryPartner=" + deliveryPartner +
                ", customerOrderItems=" + customerOrderItems +
                ", updateOrder=" + updateOrder +
                ", deliveryStatusNotes='" + deliveryStatusNotes + '\'' +
                ", deliveryPartnerID=" + deliveryPartnerID +
                ", deliveryRating=" + deliveryRating +
                ", deliveryReview='" + deliveryReview + '\'' +
                '}';
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(double totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public int getCustomerOrderID() {
        return customerOrderID;
    }

    public void setCustomerOrderID(int customerOrderID) {
        this.customerOrderID = customerOrderID;
    }

    public int getPromoCodeID() {
        return promoCodeID;
    }

    public void setPromoCodeID(int promoCodeID) {
        this.promoCodeID = promoCodeID;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public double getDeliveryChargesAmount() {
        return deliveryChargesAmount;
    }

    public void setDeliveryChargesAmount(double deliveryChargesAmount) {
        this.deliveryChargesAmount = deliveryChargesAmount;
    }

    public String getOrderStatusDateTime() {
        return orderStatusDateTime;
    }

    public void setOrderStatusDateTime(String orderStatusDateTime) {
        this.orderStatusDateTime = orderStatusDateTime;
    }

    public int getCustomerUserID() {
        return customerUserID;
    }

    public void setCustomerUserID(int customerUserID) {
        this.customerUserID = customerUserID;
    }

    public double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public double getPromoDiscountAmount() {
        return promoDiscountAmount;
    }

    public void setPromoDiscountAmount(double promoDiscountAmount) {
        this.promoDiscountAmount = promoDiscountAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getOrderReadyTimeInmin() {
        return orderReadyTimeInmin;
    }

    public void setOrderReadyTimeInmin(int orderReadyTimeInmin) {
        this.orderReadyTimeInmin = orderReadyTimeInmin;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderInstructions() {
        return orderInstructions;
    }

    public void setOrderInstructions(String orderInstructions) {
        this.orderInstructions = orderInstructions;
    }

    public Long getOrderRating() {
        return orderRating;
    }

    public void setOrderRating(Long orderRating) {
        this.orderRating = orderRating;
    }

    public String getOrderReview() {
        return orderReview;
    }

    public void setOrderReview(String orderReview) {
        this.orderReview = orderReview;
    }

    public String getOrderPickupDateTime() {
        return orderPickupDateTime;
    }

    public void setOrderPickupDateTime(String orderPickupDateTime) {
        this.orderPickupDateTime = orderPickupDateTime;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getDeliveryStatusDateTime() {
        return deliveryStatusDateTime;
    }

    public void setDeliveryStatusDateTime(Date deliveryStatusDateTime) {
        this.deliveryStatusDateTime = deliveryStatusDateTime;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public List<CustomerOrderItems> getCustomerOrderItems() {
        return customerOrderItems;
    }

    public void setCustomerOrderItems(List<CustomerOrderItems> customerOrderItems) {
        this.customerOrderItems = customerOrderItems;
    }

    public boolean isUpdateOrder() {
        return updateOrder;
    }

    public void setUpdateOrder(boolean updateOrder) {
        this.updateOrder = updateOrder;
    }

    public String getDeliveryStatusNotes() {
        return deliveryStatusNotes;
    }

    public void setDeliveryStatusNotes(String deliveryStatusNotes) {
        this.deliveryStatusNotes = deliveryStatusNotes;
    }

    public Long getDeliveryPartnerID() {
        return deliveryPartnerID;
    }

    public void setDeliveryPartnerID(Long deliveryPartnerID) {
        this.deliveryPartnerID = deliveryPartnerID;
    }

    public Long getDeliveryRating() {
        return deliveryRating;
    }

    public void setDeliveryRating(Long deliveryRating) {
        this.deliveryRating = deliveryRating;
    }

    public String getDeliveryReview() {
        return deliveryReview;
    }

    public void setDeliveryReview(String deliveryReview) {
        this.deliveryReview = deliveryReview;
    }
}