package edios.mlr.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import edios.mlr.R;
import edios.mlr.data.entities.SiteDetailsEntity;
import edios.mlr.interfaces.UpdateAppDialogListener;
import edios.mlr.model.GenericResponse;
import edios.mlr.model.LoginRequest;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements UpdateAppDialogListener {

    @BindView(R.id.user_name)
    EditText et_userName;
    @BindView(R.id.password)
    EditText et_password;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindString(R.string.app_name)
    String app_name;
    Utils utils;
    SharedPreferences prefReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (SharedPref.getReader(this).getBoolean(AppConstants.LOGIN_STATUS, false)) {
            startActivity(new Intent(this, DrawerActivity.class));
            finish();
        }
        utils = new Utils(this);
        prefReader = SharedPref.getReader(LoginActivity.this);
        tv_version.setText("Version - " + utils.getAppVersion());
    }

    @Optional
    @OnClick(R.id.login)
    public void performLogin(View view) {
        if (utils.isNetworkConnected()) {
            final MaterialDialog progressDialog = utils.showProgressDialog(AppConstants.AUTHENTICATE_MESSAGE);
            NetworkSingleton.getInstance(this).authenticate(prepareLoginRequest()).enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(@NonNull Call<GenericResponse> call,
                                       @NonNull Response<GenericResponse> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        System.out.println("response.body().getResultOutput().toString() = " + response.body().toString());
                        Toast.makeText(LoginActivity.this, response.body().getResultMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getSiteDetails() != null) {
                            storeLoginInfo(response.body().getSiteDetails());
                            if (utils.validateAppVersion(response.body().getSiteDetails())) {
                                if (response.body().getResultCode().startsWith("S")) {
                                    launchDashBoard();
                                }
                            } else {
                                utils.updateAppDialog(response.body().getSiteDetails(), LoginActivity.this);

                            }
                        }
                    } else
                        Toast.makeText(LoginActivity.this, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    System.out.println("LoginActivity.onFailure " + t.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();

                }

            });
        } else {
            Toast.makeText(this, AppConstants.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
        }

    }

    private void launchDashBoard() {
        Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
        startActivity(intent);
        finish();
    }


    private void storeLoginInfo(SiteDetailsEntity siteDetails) {
        System.out.println("siteDetails = " + siteDetails);
        SharedPreferences.Editor prefWriter = SharedPref.getEditor(LoginActivity.this);
        prefWriter.putBoolean(AppConstants.LOGIN_STATUS, true)
                .putString(AppConstants.USER_NAME, et_userName.getText().toString());
        if (siteDetails != null) {
            prefWriter.putString(AppConstants.ORDER_READY_TIME, String.valueOf(siteDetails.getOrderReadyTime()))
                    .putString(AppConstants.ORDER_NO_PREFIX, siteDetails.getOrderPrefix())
                    .putString(AppConstants.SITE_CURRENCY, siteDetails.getSiteCurrency())
                    .putString(AppConstants.SITE_TIME_ZONE, siteDetails.getTimeZone())
                    .putLong(AppConstants.ACCOUNT_ID, siteDetails.getAccountId())
                    .putLong(AppConstants.SITE_ID, siteDetails.getSiteId());
        }

        prefWriter.apply();

    }

    private LoginRequest prepareLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(et_userName.getText().toString());
        loginRequest.setUserPassword(et_password.getText().toString());
        loginRequest.setSignatureKey(AppConstants.SIGNATURE_KEY);

        return loginRequest;
    }

    @Override
    public void onPositiveClick(DialogInterface dialog, int which) {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void onNegativeClick(DialogInterface dialog, int which) {
        launchDashBoard();
        utils.saveAppVerLastCheckedAt();
    }
}
