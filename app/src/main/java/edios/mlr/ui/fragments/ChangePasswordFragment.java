package edios.mlr.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import edios.mlr.R;
import edios.mlr.model.ChangePasswordRequest;
import edios.mlr.model.GenericResponse;
import edios.mlr.network.NetworkSingleton;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment {
    @BindView(R.id.et_old_password)
    EditText et_oldPassword;
    @BindView(R.id.et_new_password)
    EditText et_newPassword;
    @BindView(R.id.et_new_password_confirm)
    EditText et_newPasswordConfirm;
    @BindView(R.id.btn_changePassword)
    Button btn_changePassword;
    private Utils utils;
    private Context context;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        utils = new Utils(getActivity());
        context = getActivity();
        return view;
    }

    @OnClick(R.id.btn_changePassword)
    public void changePassword(View view) {
        if (utils.isNetworkConnected()) {
            if (vaildatePassword()) {
                final MaterialDialog dialog = utils.showProgressDialog(AppConstants.PASSWORD_CHANGE_MESSAGE);
                NetworkSingleton.getInstance(getActivity()).changePassword(prepareChangePasswordRequest()).enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        dialog.dismiss();
                        if(response.body()!=null) {
                            if(response.body().getResultCode().startsWith("S")) {
                                Toast.makeText(getActivity(), (String) response.body().getResultOutput(), Toast.LENGTH_SHORT).show();
                                utils.launchFragment(R.id.nav_order);
                            }else {
                                Toast.makeText(context, response.body().getResultMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context, AppConstants.SERVER_NOT_RESPONDING_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        dialog.dismiss();
                        System.out.println("ChangePasswordFragment.onFailure " + t.getMessage());
                        Toast.makeText(getActivity(), AppConstants.SERVER_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(context, AppConstants.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean vaildatePassword() {

        if (et_newPassword.getText().toString().trim().length() < 5 && et_newPasswordConfirm.getText().toString().trim().length() < 6) {
            Toast.makeText(context, "Password length should be at least 5 characters.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!et_newPasswordConfirm.getText().toString().trim().equals(et_newPassword.getText().toString().trim())) {
            Toast.makeText(context, "Please confirm your new password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_oldPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Old password can't be blank.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private ChangePasswordRequest prepareChangePasswordRequest() {
        SharedPreferences pref = SharedPref.getReader(getActivity());
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUserName(pref.getString(AppConstants.USER_NAME, ""));
        request.setPassword(et_oldPassword.getText().toString());
        request.setNewPassword(et_newPasswordConfirm.getText().toString());
        request.setSignatureKey(AppConstants.SIGNATURE_KEY);
        request.setAccountID(pref.getLong(AppConstants.ACCOUNT_ID, 0l));
        request.setSiteID(pref.getLong(AppConstants.SITE_ID, 0L));
        return request;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
