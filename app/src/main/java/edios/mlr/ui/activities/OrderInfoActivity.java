package edios.mlr.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edios.mlr.R;
import edios.mlr.model.CustomerOrders;
import edios.mlr.model.DeliveryPartner;
import edios.mlr.model.GenericResponse;
import edios.mlr.model.RequestDeliveryPartnerRequest;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.ui.adapter.OrderItemsRecyclerAdapter;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class OrderInfoActivity extends AppCompatActivity {
    @BindView(R.id.profile_image)
    CircleImageView civ_customerProfilePic;
    @BindView(R.id.tv_orderInstructions)
    TextView tv_orderInstructions;
    @BindView(R.id.tv_orderNumber)
    TextView tv_orderNumber;
    @BindView(R.id.tv_orderStatus)
    TextView tv_orderStatus;
    @BindView(R.id.tv_orderType)
    TextView tv_orderType;
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
    @BindView(R.id.ib_addDP)
    ImageButton ib_addDP;
    @BindView(R.id.ib_callDP)
    ImageButton ib_callDP;
    @BindView(R.id.tv_dp)
    TextView tv_dp;
    @BindView(R.id.tv_cancelReason)
    TextView tv_cancelReason;
    @BindView(R.id.tv_deliveryAddress)
    TextView tv_deliveryAddress;
    @BindView(R.id.tv_deliveryStatus)
    TextView tv_deliveryStatus;
    @BindView(R.id.tv_deliveryStatusTime)
    TextView tv_deliveryStatusTime;
    @BindView(R.id.ll_dp)
    LinearLayout ll_dp;
    @BindView(R.id.ll_cancelReason)
    LinearLayout ll_cancelReason;
    @BindView(R.id.rb_rating)
    RatingBar rb_rating;
    @BindString(R.string.order_info_title)

    String title;
    CustomerOrders order;
    private Context context;
    private DeliveryPartner selectedDP;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        context = this;
        pref = SharedPref.getReader(context);
        ButterKnife.bind(this);
        order = getIntent().getParcelableExtra("orderInfo");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        setTitle(title);
        init();
    }

    private void init() {
        Glide.with(context).load(order.getCustomer().getUserImageURL()).into(civ_customerProfilePic);
        tv_orderNumber.setText(String.format("%s (%s)", order.getOrderNumber(), order.getOrderStatus()));
        tv_orderType.setText(order.getOrderType().equalsIgnoreCase(getString(R.string.order_type_restaurant))
                ? getString(R.string.restaurant_order).concat(" ( Table No. - " + order.getTableNumber() + " )")
                : order.getOrderType());
        tv_customerName.setText(order.getCustomer().getUserName());
        tv_customerMobile.setText(order.getCustomer().getMobileNo());
        tv_orderStatus.setText(order.getOrderDateTime());

        rv_itemRecyclerView.setAdapter(new OrderItemsRecyclerAdapter(context, order.getCustomerOrderItems(), true));
        if (order.getTotalDiscountAmount() != 0) {
            tv_totalDiscount.setVisibility(View.VISIBLE);
            tv_totalDiscount.setText("Total Discount: ".concat(AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalDiscountAmount())));
        }

        if (order.getPromoDiscountAmount() != 0) {
            tv_promoDiscount.setVisibility(View.VISIBLE);
            tv_promoDiscount.setText("Promo Code Discount: $" + Utils.decimalFormat.format(order.getPromoDiscountAmount()));
        }

        if (order.getTotalTaxAmount() != 0) {
            tv_totalTax.setVisibility(View.VISIBLE);
            tv_totalTax.setText("Total Tax: ".concat(AppConstants.CURRENCY).concat(Utils.decimalFormat.format(order.getTotalTaxAmount())));
        }
        tv_orderTotalAmount.setText(String.format("Total Amount: $%s", Utils.decimalFormat.format(order.getTotalOrderAmount())));
        tv_orderInstructions.setText(TextUtils.isEmpty(order.getOrderInstructions()) ? "N/A" : order.getOrderInstructions());

        if (order.getOrderType().equalsIgnoreCase(getString(R.string.order_type_delivery)) && order.getDeliveryAddress() != null) {
            tv_deliveryAddress.setVisibility(View.VISIBLE);
            tv_deliveryAddress.setText(order.getDeliveryAddress());
            if (order.getDeliveryPartner() != null && !TextUtils.isEmpty(order.getDeliveryStatus())) {
                tv_dp.setText(order.getDeliveryPartner().getPartnerName());
                ib_addDP.setVisibility(View.GONE);
                ib_callDP.setVisibility(View.VISIBLE);
                if (order.getDeliveryStatus().equalsIgnoreCase(getString(R.string.delivery_status_canceled))) {
                    ib_addDP.setVisibility(View.VISIBLE);
                    ll_cancelReason.setVisibility(View.VISIBLE);
                    tv_cancelReason.setText(" " + order.getDeliveryStatusNotes());
                } else {
                    ll_cancelReason.setVisibility(View.GONE);
                }
            } else {
                ib_addDP.setVisibility(View.VISIBLE);
                ib_callDP.setVisibility(View.GONE);
            }

            tv_deliveryStatus.setText(" " + (!TextUtils.isEmpty(order.getDeliveryStatus()) ? order.getDeliveryStatus() : getString(R.string.pending_order)));

            tv_deliveryStatusTime.setText(" " + (order.getDeliveryStatusDateTime() != null ? order.getDeliveryStatusDateTime().toString() : order.getOrderStatusDateTime()));
        } else {
            tv_deliveryAddress.setVisibility(View.GONE);
            ll_dp.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void assignDriver(View view) {
        Long dpId = order.getDeliveryPartner() != null
                ? order.getDeliveryPartner().getDeliveryPartnerID() : 0;

        System.out.println("order.getDeliveryPartner().getDeliveryPartnerID() = " + dpId);
        startActivityForResult(new Intent(context, AssignDeliveryPartnerActivity.class).putExtra(getString(R.string.delivery_partner), dpId), 139);
    }

    public void callDriver(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + order.getDeliveryPartner().getUserMobile()));
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            selectedDP = data.getParcelableExtra(getString(R.string.delivery_partner));
            requestDeliveryPartner();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void requestDeliveryPartner() {
        Utils utils = new Utils(context);
        final MaterialDialog dialog = utils.showProgressDialog("Requesting Delivery Partner ...");
        NetworkSingleton.getInstance(context).requestDeliveryPartner(dpRequestPayload()).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getResultCode().startsWith("S")) {
                        Toast.makeText(context, selectedDP.getPartnerName().concat(" ").concat(getString(R.string.delivery_partner_requested)), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, response.body().getResultMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(context, AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RequestDeliveryPartnerRequest dpRequestPayload() {
        RequestDeliveryPartnerRequest payload = new RequestDeliveryPartnerRequest();
        payload.setSignatureKey(AppConstants.SIGNATURE_KEY);
        payload.setAccountId(pref.getLong(AppConstants.ACCOUNT_ID, 0l));
        payload.setSiteId(pref.getLong(AppConstants.SITE_ID, 0L));
        payload.setAppEnd(AppConstants.APP_END);
        payload.setUserName(SharedPref.getReader(context).getString(AppConstants.USER_NAME, "ADMIN"));
        payload.setOrderNumber(order.getOrderNumber());
        payload.setDeliveryPartnerId(selectedDP.getDeliveryPartnerID());
        payload.setPartnerName(selectedDP.getPartnerName());
        payload.setUserFcmId(selectedDP.getUserFcmId());
        payload.setPassword(selectedDP.getPassword());
        return payload;
    }
//
//    private UpdateOrderRequest prepareUpdateOrderRequest(CustomerOrders order) {
//        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
//        updateOrderRequest.setAppEnd(AppConstants.APP_END);
//        updateOrderRequest.setDeliveryPartnerID(order.getDeliveryPartner().getDeliveryPartnerID());
//        order.setDeliveryStatus(getString(R.string.delivery_status_assigned));
//        order.setUpdateOrder(false);
//        updateOrderRequest.setOrderToBeUpdated(order);
//        updateOrderRequest.setSignatureKey(AppConstants.SIGNATURE_KEY);
//        updateOrderRequest.setUserName(SharedPref.getReader(context).getString(AppConstants.USER_NAME, "ADMIN"));
//
//        return updateOrderRequest;
//    }


}
