package edios.mlr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import edios.mlr.model.CustomerOrders;

public class ResultOutput implements Parcelable {


    @SerializedName("customerOrders")
    private List<CustomerOrders> customerOrders;

    @SerializedName("deliveryPartners")
    private List<DeliveryPartner> deliveryPartners;

    public ResultOutput(Parcel in) {
        customerOrders = in.createTypedArrayList(CustomerOrders.CREATOR);
        deliveryPartners = in.createTypedArrayList(DeliveryPartner.CREATOR);
    }

    public static final Creator<ResultOutput> CREATOR = new Creator<ResultOutput>() {
        @Override
        public ResultOutput createFromParcel(Parcel in) {
            return new ResultOutput(in);
        }

        @Override
        public ResultOutput[] newArray(int size) {
            return new ResultOutput[size];
        }
    };

    public ResultOutput() {

    }

    public List<CustomerOrders> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrders> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public List<DeliveryPartner> getDeliveryPartners() {
        return deliveryPartners;
    }

    public void setDeliveryPartners(List<DeliveryPartner> deliveryPartners) {
        this.deliveryPartners = deliveryPartners;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(customerOrders);
        dest.writeTypedList(deliveryPartners);
    }

    @Override
    public String toString() {
        return "ResultOutput{" +
                "customerOrders=" + customerOrders +
                ", deliveryPartners=" + deliveryPartners +
                '}';
    }
}