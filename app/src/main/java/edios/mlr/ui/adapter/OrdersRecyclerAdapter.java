package edios.mlr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edios.mlr.R;
import edios.mlr.interfaces.NotifyFetchOrder;
import edios.mlr.model.CustomerOrders;
import edios.mlr.model.FetchOrderResponse;
import edios.mlr.model.ResultOutput;
import edios.mlr.model.UpdateOrderRequest;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.ui.activities.OrderInfoActivity;
import edios.mlr.utils.AlertDialogSingleton;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;
import edios.mlr.utils.printing.BluetoothService;
import edios.mlr.utils.printing.SingletonBluetoothService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.ViewHolder> {

    String[] print_size_array;
    private Context context;
    private List<CustomerOrders> customerOrders;
    private Utils utils;
    private boolean showUpdateStatus;

    public OrdersRecyclerAdapter(Context context, List<CustomerOrders> customerOrders, boolean showUpdateStatus) {
        this.context = context;
        this.customerOrders = customerOrders;
        this.utils = new Utils(context);
        this.showUpdateStatus = showUpdateStatus;
        print_size_array = context.getResources().getStringArray(R.array.print_size_array);

    }

    public void updateList(List<CustomerOrders> customerOrders) {
        this.customerOrders = customerOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_recycler_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CustomerOrders order = customerOrders.get(position);
        holder.tv_orderNumber.setText(order.getOrderNumber() + " (" + order.getOrderStatus() + ")");

        String orderType = order.getOrderType().equalsIgnoreCase(context.getString(R.string.order_type_restaurant))
                ? context.getString(R.string.restaurant_order).concat(" ( Table No. - " + order.getTableNumber() + " )")
                : order.getOrderType();

        String orderDeliveryStatus = "<font color='#536dfe'>" + (!TextUtils.isEmpty(order.getDeliveryStatus()) ? order.getDeliveryStatus()
                : context.getString(R.string.pending_order)) + "</font>";
        holder.tv_orderType.setText(Html.fromHtml(orderType.concat(orderType.equalsIgnoreCase(context.getString(R.string.order_type_delivery))
                ? " ( " + orderDeliveryStatus + " ) " : "")));

        if (order.getOrderType().equals(context.getString(R.string.order_type_restaurant))) {
            holder.tv_orderType.setTextColor(context.getResources().getColor(R.color.colorRestaurant));
        } else if (order.getOrderType().equals(context.getString(R.string.order_type_take_away))) {
            holder.tv_orderType.setTextColor(context.getResources().getColor(R.color.colorTakeAway));
        } else if (order.getOrderType().equals(context.getString(R.string.order_type_delivery))) {
            holder.tv_orderType.setTextColor(context.getResources().getColor(R.color.colorDelivery));
        }

        holder.tv_orderStatus.setText(order.getOrderDateTime());
        holder.rv_itemRecyclerView.setAdapter(new OrderItemsRecyclerAdapter(context, order.getCustomerOrderItems(), false));
        holder.tv_customerName.setText(order.getCustomer().getUserName());
        holder.tv_customerMobile.setText(order.getCustomer().getMobileNo());
        updateRating(holder, order);

        if (order.getTotalDiscountAmount() != 0) {
            holder.tv_totalDiscount.setVisibility(View.VISIBLE);
            holder.tv_totalDiscount.setText("Total Discount: $" + Utils.decimalFormat.format(order.getTotalDiscountAmount()));
        } else {
            holder.tv_totalDiscount.setVisibility(View.GONE);
        }

        if (order.getPromoDiscountAmount() != 0) {
            holder.tv_promoDiscount.setVisibility(View.VISIBLE);
            holder.tv_promoDiscount.setText("Promo Code Discount: $" + Utils.decimalFormat.format(order.getPromoDiscountAmount()));
        } else {
            holder.tv_promoDiscount.setVisibility(View.GONE);
        }

        if (order.getTotalTaxAmount() != 0) {
            holder.tv_totalTax.setVisibility(View.VISIBLE);
            holder.tv_totalTax.setText("Total Tax: $" + Utils.decimalFormat.format(order.getTotalTaxAmount()));
        } else {
            holder.tv_totalTax.setVisibility(View.GONE);
        }
        holder.tv_orderTotalAmount.setText("Total Amount: $" + Utils.decimalFormat.format(order.getTotalOrderAmount()));
    }

    private void updateRating(ViewHolder holder, CustomerOrders order) {
        if (order.getOrderRating() != null && order.getOrderRating() > 0) {
            holder.ll_orderRating.setVisibility(View.VISIBLE);
            holder.tv_orderRating.setText(Html.fromHtml("Order Rating - <font color='#536dfe'>" + order.getOrderRating() + "</font>/5"));
            if (!TextUtils.isEmpty(order.getOrderReview())) {
                holder.tv_orderReview.setVisibility(View.VISIBLE);
                holder.tv_orderReview.setText(order.getOrderReview());
            } else holder.tv_orderReview.setVisibility(View.GONE);
        } else holder.ll_orderRating.setVisibility(View.GONE);

        if (order.getDeliveryRating() != null && order.getDeliveryRating() > 0 && order.getOrderType().equalsIgnoreCase(context.getString(R.string.orderType_Delivery))) {
            holder.ll_deliveryRating.setVisibility(View.VISIBLE);
            holder.tv_deliveryRating.setText(Html.fromHtml("Delivery Rating - <font color='#536dfe'>" + order.getDeliveryRating() + "</font>/5"));
            if (!TextUtils.isEmpty(order.getDeliveryReview())) {
                holder.tv_deliveryReview.setVisibility(View.VISIBLE);
                holder.tv_deliveryReview.setText(order.getDeliveryReview());
            } else holder.tv_deliveryReview.setVisibility(View.GONE);
        } else holder.ll_deliveryRating.setVisibility(View.GONE);
    }

    private UpdateOrderRequest prepareUpdateOrderRequest(CustomerOrders order, String status) {
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
        updateOrderRequest.setAppEnd(AppConstants.APP_END);
        order.setOrderStatus(status);
        order.setUpdateOrder(true);
        order.setDeliveryPartnerID(order.getDeliveryPartner() != null ? order.getDeliveryPartner().getDeliveryPartnerID() : null);
        updateOrderRequest.setOrderToBeUpdated(order);
        updateOrderRequest.setSignatureKey(AppConstants.SIGNATURE_KEY);
        updateOrderRequest.setUserName(SharedPref.getReader(context).getString(AppConstants.USER_NAME, "ADMIN"));
        System.out.println("updateOrderRequest = " + updateOrderRequest);
        return updateOrderRequest;
    }

    @Override
    public int getItemCount() {
        return customerOrders != null ? customerOrders.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_orderNumber)
        TextView tv_orderNumber;
        @BindView(R.id.tv_orderType)
        TextView tv_orderType;
        @BindView(R.id.tv_orderStatus)
        TextView tv_orderStatus;
        @BindView(R.id.tv_customerName)
        TextView tv_customerName;
        @BindView(R.id.tv_customerMobile)
        TextView tv_customerMobile;
        @BindView(R.id.tv_orderTotalAmount)
        TextView tv_orderTotalAmount;
        @BindView(R.id.tv_totalDiscount)
        TextView tv_totalDiscount;
        @BindView(R.id.tv_promoDiscount)
        TextView tv_promoDiscount;
        @BindView(R.id.tv_totalTax)
        TextView tv_totalTax;
        @BindView(R.id.rv_items)
        RecyclerView rv_itemRecyclerView;
        @BindView(R.id.btn_updateStatus)
        Button btn_updateStatus;
        @BindView(R.id.btn_printOrder)
        Button btn_printOrder;
        @BindView(R.id.tv_orderRating)
        TextView tv_orderRating;
        @BindView(R.id.tv_orderReview)
        TextView tv_orderReview;
        @BindView(R.id.tv_deliveryRating)
        TextView tv_deliveryRating;
        @BindView(R.id.tv_deliveryReview)
        TextView tv_deliveryReview;
        @BindView(R.id.ll_orderRating)
        LinearLayout ll_orderRating;
        @BindView(R.id.ll_deliveryRating)
        LinearLayout ll_deliveryRating;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (!showUpdateStatus) {
                btn_updateStatus.setVisibility(View.GONE);
                btn_printOrder.setVisibility(View.GONE);
            }

        }

        @OnClick({R.id.btn_updateStatus, R.id.btn_printBill, R.id.btn_printOrder, R.id.iv_orderInfo, R.id.tv_orderReview, R.id.tv_deliveryReview})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_updateStatus:
                    int index = Arrays.asList(context.getResources().getStringArray(R.array.status_array)).
                            indexOf(customerOrders.get(getAdapterPosition()).getOrderStatus());
                    utils.updateStatusDialog(index, (dialog, itemView, which, text) -> {
                        updateOrder(text.toString());
                        return false;
                    });
                    break;
                case R.id.iv_orderInfo:
                    Intent orderInfoIntent = new Intent(context, OrderInfoActivity.class);
                    orderInfoIntent.putExtra("orderInfo", customerOrders.get(getAdapterPosition()));
                    context.startActivity(orderInfoIntent);
                    break;
                case R.id.btn_printBill:
                    if (SingletonBluetoothService.getInstance(context, null).mState == BluetoothService.STATE_CONNECTED) {
                        String selectedPrintSize = SharedPref.getReader(context).getString(AppConstants.PRINT_SIZE, print_size_array[0]);
                        if (selectedPrintSize.equalsIgnoreCase(print_size_array[0]))
                            utils.print3InchTSPBill(customerOrders.get(getAdapterPosition()));
                        else if (selectedPrintSize.equalsIgnoreCase(print_size_array[1]))
                            utils.print3InchBill(customerOrders.get(getAdapterPosition()));
                        else utils.print2InchBill(customerOrders.get(getAdapterPosition()));
                    } else {
                        Toast.makeText(context, AppConstants.BLUETOOTH_UNAVAILABLE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_printOrder:
                    if (SingletonBluetoothService.getInstance(context, null).mState == BluetoothService.STATE_CONNECTED) {
                        String selectedPrintSize = SharedPref.getReader(context).getString(AppConstants.PRINT_SIZE, print_size_array[0]);
                        if (selectedPrintSize.equalsIgnoreCase(print_size_array[0]))
                            utils.print3InchTSPKitchenTicket(customerOrders.get(getAdapterPosition()));
                        else if (selectedPrintSize.equalsIgnoreCase(print_size_array[1]))
                            utils.print3InchKitchenTicket(customerOrders.get(getAdapterPosition()));
                        else
                            utils.print2InchKitchenTicket(customerOrders.get(getAdapterPosition()));
                    } else {
                        Toast.makeText(context, AppConstants.BLUETOOTH_UNAVAILABLE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tv_orderReview:
                    AlertDialogSingleton.alertDialogShow(context, "Order Review", customerOrders.get(getAdapterPosition()).getOrderReview(), true, null);
                    break;

                case R.id.tv_deliveryReview:
                    AlertDialogSingleton.alertDialogShow(context, "Delivery Review", customerOrders.get(getAdapterPosition()).getDeliveryReview(), true, null);
                    break;
            }
        }

        private boolean validateStatusUpdate(CustomerOrders order, String updatingStatus) {
            if (order.getOrderStatus().equalsIgnoreCase(context.getString(R.string.completed_order)) && !updatingStatus.equalsIgnoreCase(context.getString(R.string.completed_order))) {
                Toast.makeText(context, context.getString(R.string.IncompleteOrderErrMsg), Toast.LENGTH_LONG).show();
                return false;
            } else if (order.getOrderType().equalsIgnoreCase(context.getString(R.string.orderType_Delivery))) {
                if ((TextUtils.isEmpty(order.getDeliveryStatus()) || (!TextUtils.isEmpty(order.getDeliveryStatus()) && !order.getDeliveryStatus().equalsIgnoreCase(context.getString(R.string.deliveryStatus_Delivered))))
                        && updatingStatus.equalsIgnoreCase(context.getString(R.string.completed_order))) {
                    Toast.makeText(context, context.getString(R.string.CompleteOrderWithoutDPErrMsg), Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            return true;
        }


        void updateOrder(String orderStatus) {
            CustomerOrders odr = customerOrders.get(getAdapterPosition());
            if (validateStatusUpdate(odr, orderStatus)) {
                final MaterialDialog dialog = utils.showProgressDialog("Updating Order ...");
                NetworkSingleton.getInstance(context).updateOrder(prepareUpdateOrderRequest(odr, orderStatus)).enqueue(new Callback<FetchOrderResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FetchOrderResponse> call, @NonNull Response<FetchOrderResponse> response) {
                        dialog.dismiss();
                        System.out.println("response = " + response.toString());
                        if (response.body() != null) {
                            new Utils(context).fetchOrders((orders, isSuccess) -> context.sendBroadcast(new Intent(AppConstants.UPDATE_ORDERS).putExtra(AppConstants.UPDATE_ORDERS, orders)), "", "");
                        } else {
                            Toast.makeText(context, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FetchOrderResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(context, AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}
