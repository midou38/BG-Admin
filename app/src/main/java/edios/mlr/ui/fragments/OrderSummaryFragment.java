package edios.mlr.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import edios.mlr.R;
import edios.mlr.model.OrderSummaryItem;
import edios.mlr.model.OrderSummaryRequest;
import edios.mlr.model.OrderSummaryResponse;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.ui.adapter.OrderSummaryAdapter;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.StartDatePicker;
import edios.mlr.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummaryFragment extends Fragment {

    @BindView(R.id.tv_from_date)
    TextView tv_fromDate;
    @BindView(R.id.tv_to_date)
    TextView tv_toDate;
    @BindView(R.id.total_summary_amount)
    TextView tv_totalSummaryAmount;
    @BindView(R.id.tv_noOrderFound)
    TextView tv_noOrderFound;
    Utils utils;
    @BindView(R.id.order_summary_recycler)
    RecyclerView recyclerView;
    Context context;
    View rootView;
    double totalAmount;
    private Unbinder unbinder;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_summary, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        context = getActivity();
        pref = SharedPref.getReader(context);
        utils = new Utils(context);
        tv_fromDate.setText(utils.getFirstDateOfMonth());
        tv_toDate.setText(utils.getCurrentDate());
        fetchOrderSummary();
        return rootView;
    }

    @OnClick({R.id.iv_order_summary, R.id.tv_from_date, R.id.tv_to_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_order_summary:
                if (utils.isNetworkConnected()) fetchOrderSummary();
                else
                    Toast.makeText(context, AppConstants.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_from_date:
                new StartDatePicker(getActivity(), tv_fromDate).show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "start_date_picker");
                break;
            case R.id.tv_to_date:
                new StartDatePicker(getActivity(), tv_toDate).show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "start_date_picker");
                break;
        }

    }

    private void fetchOrderSummary() {
        final MaterialDialog dialog = utils.showProgressDialog(AppConstants.ORDER_SUMMARY_MESSAGE);
        NetworkSingleton.getInstance(context).orderSummary(prepareOrderSummaryRequest()).enqueue(new Callback<OrderSummaryResponse>() {
            @Override
            public void onResponse(Call<OrderSummaryResponse> call, Response<OrderSummaryResponse> response) {

                totalAmount = 0;
                if (response.body() != null) {
                    System.out.println("ORDER SUMMARY = " + response.body().toString());
                    if (response.body().getResultOutput() instanceof List<?>) {
                        recyclerView.setAdapter(new OrderSummaryAdapter(context, (List<OrderSummaryItem>) response.body().getResultOutput()));
                        tv_noOrderFound.setVisibility(recyclerView.getAdapter().getItemCount() < 1 ? View.VISIBLE : View.GONE);
                        for (OrderSummaryItem item : (List<OrderSummaryItem>) response.body().getResultOutput()) {
                            totalAmount += item.getTotalAmount();
                        }
                        tv_totalSummaryAmount.setText("Total Amount: $" + Utils.decimalFormat.format(totalAmount));

                    } else
                        Toast.makeText(context, response.body().getResultOutput().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();


            }

            @Override
            public void onFailure(Call<OrderSummaryResponse> call, Throwable t) {
                System.out.println("OrderSummaryFragment.onFailure");
                dialog.dismiss();
                Toast.makeText(context, AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private OrderSummaryRequest prepareOrderSummaryRequest() {
        OrderSummaryRequest request = new OrderSummaryRequest();
        request.setSignatureKey(AppConstants.SIGNATURE_KEY);
        request.setAccountID(pref.getLong(AppConstants.ACCOUNT_ID, 0L));
        request.setSiteID(pref.getLong(AppConstants.SITE_ID, 0L));
        request.setUserName(pref.getString(AppConstants.USER_NAME, ""));
        request.setFromDate(utils.appDateToApiDate(tv_fromDate.getText().toString()));
        request.setToDate(utils.appDateToApiDate(tv_toDate.getText().toString()));
        System.out.println("request = " + request);
        return request;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
