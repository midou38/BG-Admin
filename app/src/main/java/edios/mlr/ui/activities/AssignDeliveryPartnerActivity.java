package edios.mlr.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import edios.mlr.R;
import edios.mlr.interfaces.SelectDeliveryPartner;
import edios.mlr.model.DeliveryPartner;
import edios.mlr.model.FetchOrderResponse;
import edios.mlr.model.ResultOutput;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.ui.adapter.DeliveryPartnerRecyclerAdapter;
import edios.mlr.utils.AlertDialogSingleton;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignDeliveryPartnerActivity extends AppCompatActivity implements SelectDeliveryPartner {

    @BindView(R.id.rv_availDriver)
    RecyclerView rv_availDriver;
    @BindView(R.id.tv_noDp)
    TextView tv_noDp;

    private Utils utils;
    private Context context;
    private Long currentDPID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_delivery_partner);
        init();
    }

    private void init() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        setTitle(getString(R.string.assign_delivery_partner));
        context = this;
        utils = new Utils(context);
        ButterKnife.bind(this);
        currentDPID = getIntent().getLongExtra(getString(R.string.delivery_partner), 0);
        System.out.println("currentDPID = " + currentDPID);
        getDeliveryPartner();
    }

    private void getDeliveryPartner() {
        if (utils.isNetworkConnected()) {
            final MaterialDialog progressDialog = utils.showProgressDialog(AppConstants.FETCHING_ORDERS_MESSAGE);
            NetworkSingleton.getInstance(context).fetchDeliveryPartners(utils.prepareFetchOrderRequest("")).enqueue(new Callback<FetchOrderResponse>() {
                @Override
                public void onResponse(@NonNull Call<FetchOrderResponse> call, @NonNull Response<FetchOrderResponse> response) {
                    System.out.println("Utils.onResponse");
                    if (progressDialog != null) progressDialog.dismiss();
                    if (response.body() != null) {
                        setupRecycler(filterDPs(response.body().getResultOutput()));
                    } else
                        Toast.makeText(context, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<FetchOrderResponse> call, @NonNull Throwable t) {
                    System.out.println("OrdersFragment.onFailure " + t.getMessage());
                    Toast.makeText(context, AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    if (progressDialog != null) progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(context, AppConstants.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecycler(List<DeliveryPartner> deliveryPartners) {
        tv_noDp.setVisibility(deliveryPartners != null && deliveryPartners.size() > 0 ? View.GONE : View.VISIBLE);
        rv_availDriver.setLayoutManager(new LinearLayoutManager(context));
        rv_availDriver.setAdapter(new DeliveryPartnerRecyclerAdapter(context, deliveryPartners,
                AssignDeliveryPartnerActivity.this));
    }

    private List<DeliveryPartner> filterDPs(ResultOutput resultOutput) {
        List<DeliveryPartner> filteredDP = new ArrayList<>();
        List<DeliveryPartner> deliveryPartners = resultOutput.getDeliveryPartners();
        if (deliveryPartners != null && deliveryPartners.size() > 0) {
            for (DeliveryPartner dp : deliveryPartners) {
                if (!dp.getDeliveryPartnerID().equals(currentDPID)) {
                    filteredDP.add(dp);
                }
            }
        }
        return filteredDP;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adp_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_refreshDP) {
            getDeliveryPartner();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getSelectedPartner(final DeliveryPartner partner) {
        String message = getString(R.string.select_delivery_partners_1).concat(" ").concat(partner.getPartnerName()).concat(" ").concat(getString(R.string.select_delivery_partners_2));
        AlertDialogSingleton.alertDialogShow(context, getString(R.string.assign_delivery_partner), message, false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    setResult(RESULT_OK, new Intent().putExtra(getString(R.string.delivery_partner), partner));
                    finish();
                }
            }
        });
    }
}
