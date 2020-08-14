package edios.mlr.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import edios.mlr.R;
import edios.mlr.data.db.TOIAdminDB;
import edios.mlr.data.entities.SiteDetailsEntity;
import edios.mlr.model.GenericResponse;
import edios.mlr.model.SettingsRequest2;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.utils.AlertDialogSingleton;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.switch_auto_print)
    SwitchCompat switch_autoPrint;
    @BindView(R.id.switch_euShutdown)
    SwitchCompat switch_euShutdown;
    @BindView(R.id.et_refresh_time)
    EditText et_refreshTime;
    @BindView(R.id.et_ready_time)
    EditText et_readyTime;
    @BindView(R.id.et_deliveryCharges)
    EditText et_deliveryCharges;
    @BindView(R.id.et_deliveryRadius)
    EditText et_deliveryRadius;
    @BindView(R.id.spin_deliveryCharges)
    Spinner spin_deliveryCharges;
    @BindView(R.id.spin_orderType)
    Spinner spin_orderType;
    @BindView(R.id.et_delivery_pick_time)
    EditText et_delivery_pick_time;
    @BindView(R.id.til_delivery_pick_time)
    TextInputLayout til_delivery_pick_time;
    @BindView(R.id.ll_delivery)
    LinearLayout ll_delivery;
    @BindView(R.id.til_deliveryCharges)
    TextInputLayout til_deliveryCharges;

    @BindView(R.id.print_size_spinner)
    AppCompatSpinner spin_printSize;
    @BindString(R.string.print_2_inch)
    String print_2_inch;
    @BindString(R.string.print_3_inch)
    String print_3_inch;
    @BindString(R.string.print_tsp_3_inch)
    String print_tsp_3_inch;

    @BindString(R.string.delivery_charges_fixed)
    String delivery_charges_fixed;
    @BindString(R.string.delivery_charges_bill)
    String delivery_charges_bill;

    @BindString(R.string.yes)
    String yes;
    @BindString(R.string.no)
    String no;


    @BindArray(R.array.print_size_array)
    String[] print_size_array;

    private SharedPreferences pref;
    private Context context;
    private Utils utils;
    private Unbinder unbinder;
    private SiteDetailsEntity siteDetails;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        initSettings();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initSettings() {
        context = getActivity();
        siteDetails = TOIAdminDB.getInstance(context).getSiteDetails();
        utils = new Utils(context);
        pref = SharedPref.getReader(context);
        et_readyTime.setText(siteDetails.getOrderReadyTime().toString());

        switch_autoPrint.setChecked(pref.getBoolean(AppConstants.AUTO_PRINT, false));

        switch_euShutdown.setChecked(siteDetails.getEuAppShutdown().equals(yes));

        et_refreshTime.setText(Integer.toString(pref.getInt(AppConstants.ORDER_REFRESH_TIME, AppConstants.DEFAULT_REFRESH_TIME)));
        String selectedPrintSize = pref.getString(AppConstants.PRINT_SIZE, print_tsp_3_inch);
        List<String> printSize = Arrays.asList(print_size_array);
        spin_printSize.setSelection(printSize.indexOf(selectedPrintSize));

        et_deliveryCharges.setText(siteDetails.getDeliveryCharges().toString());
        et_deliveryRadius.setText(siteDetails.getDeliveryRadius().toString());
        et_delivery_pick_time.setText(siteDetails.getOrderDeliveryTime().toString());

        String deliveryCharges = siteDetails.getDeliveryCharges().toString();
        List<String> dcList = Arrays.asList(getResources().getStringArray(R.array.delivery_charges));
        spin_deliveryCharges.setSelection(dcList.indexOf(deliveryCharges));

        String orderType = siteDetails.getOrderDeliveryType();
        List<String> otList = Arrays.asList(getResources().getStringArray(R.array.orderTypes));
        spin_orderType.setSelection(otList.indexOf(orderType));

        switch_euShutdown.setOnCheckedChangeListener(this);
        setupDeliveryCharges();
        setupOrderType();
    }

    private void setupDeliveryCharges() {
        spin_deliveryCharges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] deliveryCharges = getResources().getStringArray(R.array.delivery_charges);
                if (deliveryCharges[position].equals(delivery_charges_fixed)) {
                    et_deliveryCharges.setEnabled(true);
                    til_deliveryCharges.setHint(getString(R.string.delivery_charges_fixed_hint));
                } else if (deliveryCharges[position].equals(delivery_charges_bill)) {
                    et_deliveryCharges.setEnabled(true);
                    til_deliveryCharges.setHint(getString(R.string.delivery_charges_percent_hint));
                } else {
                    et_deliveryCharges.setEnabled(false);
                    til_deliveryCharges.setHint(getString(R.string.delivery_charges_fixed_hint));
                    et_deliveryCharges.setText(String.valueOf(0));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupOrderType() {
        spin_orderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] orderType = getResources().getStringArray(R.array.orderTypes);
                if (orderType[position].equals(getString(R.string.orderType_Both))) {
                    til_delivery_pick_time.setVisibility(View.VISIBLE);
                    ll_delivery.setVisibility(View.VISIBLE);
                } else if (orderType[position].equals(getString(R.string.orderType_Delivery))) {
                    til_delivery_pick_time.setVisibility(View.VISIBLE);
                    ll_delivery.setVisibility(View.VISIBLE);
                } else if (orderType[position].equals(getString(R.string.orderType_TakeAway))) {
                    til_delivery_pick_time.setVisibility(View.GONE);
                    ll_delivery.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @OnClick(R.id.btn_update_settings)
    public void onClick(View view) {
        updateSettingConfirmation();
    }

    private boolean validateSettings() {
        if (et_readyTime.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Order Ready Time (In Minutes) can't be blank.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_refreshTime.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Dashboard Refresh Time (In Seconds) can't be blank.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            int readyTime = Integer.parseInt(et_readyTime.getText().toString());
            int refreshTime = Integer.parseInt(et_refreshTime.getText().toString());
            if (readyTime < 5 || readyTime > 60) {
                Toast.makeText(context, "Order Ready Time should be between 5 to 60 Minutes.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (refreshTime < 15 || refreshTime > 60) {
                Toast.makeText(context, "Dashboard Refresh Time should be between 15 to 60 Seconds.", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
    }

    private void saveSettings() {
        SharedPref.getEditor(context)
                .putString(AppConstants.PRINT_SIZE, spin_printSize.getSelectedItem().toString())
                .putInt(AppConstants.ORDER_REFRESH_TIME, Integer.parseInt(et_refreshTime.getText().toString()))
                .putBoolean(AppConstants.AUTO_PRINT, switch_autoPrint.isChecked()).apply();
        utils.launchFragment(R.id.nav_order);
    }

    private SettingsRequest2 prepareSettingsRequest() {
        SettingsRequest2 request = new SettingsRequest2();
        request.setUserName(pref.getString(AppConstants.USER_NAME, ""));
        request.setSignatureKey(AppConstants.SIGNATURE_KEY);
        request.setAccountID(pref.getLong(AppConstants.ACCOUNT_ID, 0l));
        request.setSiteID(pref.getLong(AppConstants.SITE_ID, 0L));
        request.setEuAppShutdown(switch_euShutdown.isChecked() ? getString(R.string.yes) : getString(R.string.no));
        request.setReadyTimeInMin(et_readyTime.getText().toString().trim());
        request.setDeliveryChargesType(spin_deliveryCharges.getSelectedItem().toString());
        request.setDeliveryChargesAmount(!TextUtils.isEmpty(et_deliveryCharges.getText().toString()) ? Float.parseFloat(et_deliveryCharges.getText().toString()) : 0.0f);
        request.setDeliveryRadius(!TextUtils.isEmpty(et_deliveryRadius.getText().toString()) ? Long.parseLong(et_deliveryRadius.getText().toString()) : null);
        request.setOrderType(spin_orderType.getSelectedItem().toString());
        request.setDeliveryTime(!TextUtils.isEmpty(et_delivery_pick_time.getText()) ? Long.parseLong(et_delivery_pick_time.getText().toString()) : 0L);
        return request;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        AlertDialogSingleton.alertDialogShow
                (context, null, isChecked
                        ? getString(R.string.shutdown_msg)
                        : getString(R.string.enable_app_msg), true, null);
    }

    private void updateSettingConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.setting_confirm_dialog, null);
        TextView et_settingConfirmation = view.findViewById(R.id.et_settingConfirmation);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setTitle(getString(R.string.update_settings));
        dialog.setMessage(getString(R.string.please_enter_toi_to_update_the_settings));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", (dialog1, which) -> {
            if (et_settingConfirmation.getText().toString().equals(getString(R.string.toi)))
                updateSettings();
            else {
                dialog.dismiss();
                Toast.makeText(context, R.string.invalid_security_text, Toast.LENGTH_LONG).show();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog1, which) -> dialog.dismiss());
        dialog.show();
    }

    private void updateSettings() {
        if (validateSettings())
            if (utils.isNetworkConnected()) {
                final MaterialDialog dialog = utils.showProgressDialog(AppConstants.UPDATE_SETTING_MESSAGE);
                NetworkSingleton.getInstance(context).updateSettingsV2(prepareSettingsRequest()).enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                        dialog.dismiss();
                        if (response.body() != null) {
                            if (response.body().getResultCode().startsWith("S")) {
                                saveSettings();
                            }
                            Toast.makeText(context, response.body().getResultMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(context, AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, AppConstants.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
            }
    }


}
