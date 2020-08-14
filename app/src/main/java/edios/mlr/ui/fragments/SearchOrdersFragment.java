package edios.mlr.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import edios.mlr.R;
import edios.mlr.model.FetchOrderResponse;
import edios.mlr.model.SearchOrderRequest;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.ui.adapter.OrdersRecyclerAdapter;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchOrdersFragment extends Fragment {
    @BindView(R.id.et_customer_name)
    EditText et_customer_name;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_order)
    EditText et_order_no;
    @BindView(R.id.filter_layout)
    LinearLayout ll_filter_layout;
    @BindView(R.id.tv_noOrderFound)
    TextView tv_noOrderFound;
    @BindView(R.id.rv_searchOrder)
    RecyclerView rv_searchOrder;
    private boolean showFilterLayout = false;
    private Context context;
    private Utils utils;
    private Unbinder unbinder;
    private SharedPreferences pref;
    private String orderType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_orders, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        context = getActivity();
        pref = SharedPref.getReader(context);
        utils = new Utils(context);
        orderType = SharedPref.getReader(getContext()).getString(AppConstants.ORDER_TYPE, "");
        SharedPref.getEditor(getActivity()).putString(AppConstants.ORDER_TYPE, "").apply();
        return rootView;
    }

    @OnClick(R.id.search_order)
    public void onClick(View view) {
        if (utils.isNetworkConnected()) {
            if (showFilterLayout) {
                showFilterLayout = false;
                ll_filter_layout.setVisibility(View.VISIBLE);
            } else {
                showFilterLayout = true;
                searchOrders();
            }
        } else {
            Toast.makeText(context, AppConstants.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
        }

    }

    private void searchOrders() {
        final MaterialDialog dialog = utils.showProgressDialog(AppConstants.SEARCH_ORDER_MESSAGE);
        NetworkSingleton.getInstance(context).searchOrders(prepareSearchOrderRequest()).enqueue(new Callback<FetchOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchOrderResponse> call, @NonNull Response<FetchOrderResponse> response) {
                if (response.body() != null) {
                    rv_searchOrder.setAdapter(new OrdersRecyclerAdapter(getActivity(), response.body().getResultOutput().getCustomerOrders(), false));
                    tv_noOrderFound.setVisibility(rv_searchOrder.getAdapter().getItemCount() < 1 ? View.VISIBLE : View.GONE);
                    ll_filter_layout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<FetchOrderResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                ll_filter_layout.setVisibility(View.GONE);
                Toast.makeText(context, AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private SearchOrderRequest prepareSearchOrderRequest() {
        SearchOrderRequest request = new SearchOrderRequest();
        request.setAccountID(pref.getLong(AppConstants.ACCOUNT_ID, 0l));
        request.setSiteID(pref.getLong(AppConstants.SITE_ID, 0L));
        request.setCustomerName(et_customer_name.getText().toString().trim());
        request.seteMailAddress(et_email.getText().toString().trim());
        request.setMobileNo(et_mobile.getText().toString().trim());
        request.setOrderNo(et_order_no.getText().toString().trim());
        request.setSignatureKey(AppConstants.SIGNATURE_KEY);
        request.setUserName(SharedPref.getReader(context).getString(AppConstants.USER_NAME, ""));

        return request;
    }

    @Override
    public void onDestroyView() {
        SharedPref.getEditor(getActivity()).putString(AppConstants.ORDER_TYPE, orderType).apply();
        super.onDestroyView();
        unbinder.unbind();
    }
}
