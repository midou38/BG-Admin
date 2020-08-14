package edios.mlr.ui.activities;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import edios.mlr.R;
import edios.mlr.interfaces.BluetoothConnectivity;
import edios.mlr.interfaces.UpdateAppDialogListener;
import edios.mlr.ui.fragments.OrdersFragment;
import edios.mlr.utils.AlertDialogSingleton;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;
import edios.mlr.utils.printing.BluetoothService;
import edios.mlr.utils.printing.DeviceListActivity;
import edios.mlr.utils.printing.SingletonBluetoothService;

public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, BluetoothConnectivity, UpdateAppDialogListener {
    @BindString(R.string.log_out_message)
    String logOutMsg;
    @BindColor(R.color.colorPrimary)
    int colorAccent;
    @BindView(R.id.tv_loggedIn)
    TextView tv_loggedIn;
    Utils utils;
    NavigationView navigationView;
    private boolean doubleBackToExitPressedOnce;
    private BluetoothService mService;
    private BluetoothDevice con_dev;
    private MaterialDialog btDialog;
    private Menu menu;
    private ImageView navHeadImage = null;
    private BroadcastReceiver  headerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("DrawerActivity.onReceive");
            if (intent != null &&
                    Objects.requireNonNull(intent.getAction()).equals(getString(R.string.header_broadcaster))) {
                if(navHeadImage == null){
                 navHeadImage = navigationView.inflateHeaderView(R.layout.nav_header_drawer)
                        .findViewById(R.id.navHead_image);}
                Glide.with(DrawerActivity.this)
                        .load(intent.getStringExtra(getString(R.string.header_image_url)))
                        .placeholder(getDrawable(R.drawable.restaurant))
                        .into(navHeadImage);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        SharedPref.getEditor(DrawerActivity.this).putString(AppConstants.ORDER_TYPE, AppConstants.ORDER_TYPE_TAKE_AWAY).apply();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        utils = new Utils(this);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        utils.launchFragment(R.id.nav_order);
        mService = SingletonBluetoothService.getInstance(this, this);
        tv_loggedIn.setText("Logged In As: " + SharedPref.getReader(this).getString(AppConstants.USER_NAME, ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(headerReceiver, new IntentFilter(getString(R.string.header_broadcaster)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(headerReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            System.out.println(Objects.requireNonNull(f).getTargetFragment() + "" + (f instanceof OrdersFragment));
            if (f instanceof OrdersFragment) {
                exitApp();
            } else {
                navigationView.setCheckedItem(R.id.nav_order);
                utils.launchFragment(R.id.nav_order);
            }
        }
    }

    private void exitApp() {
        if (doubleBackToExitPressedOnce) this.finish();

        doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        View parentLayout = getWindow().getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(parentLayout, "Please click BACK again to exit", Snackbar.LENGTH_SHORT);
        snackbar.setAction("DISMISS", view -> {
            snackbar.dismiss();
            doubleBackToExitPressedOnce = false;
        }).setActionTextColor(colorAccent);

        snackbar.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Optional
    @OnClick(R.id.btn_log_out)
    public void logoutClick(View view) {
        AlertDialogSingleton.alertDialogShow(this, null, logOutMsg, false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(DrawerActivity.this, LoginActivity.class));
                SharedPref.getEditor(DrawerActivity.this).putBoolean(AppConstants.LOGIN_STATUS, false).apply();
                finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        utils.launchFragment(item.getItemId());
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);

        if (mService.getState() == BluetoothService.STATE_CONNECTED)
            menu.findItem(R.id.bluetooth).setIcon(R.drawable.ic_bluetooth_connect);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bluetooth) {
            if (mService.isBTopen()) {
                startActivityForResult(new Intent(getApplicationContext(), DeviceListActivity.class), 1);
            } else
                startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 2);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 1) {
                if (data.hasExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS)) {
                    btDialog = utils.showProgressDialog("Connecting printer... ");
                    con_dev = mService.getDevByMac(Objects.requireNonNull(data.getExtras()).getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS));
                    mService.connect(con_dev);
                }
            } else if (requestCode == 2) { //after bluetooth is enabled
                startActivityForResult(new Intent(getApplicationContext(), DeviceListActivity.class), 1);
            }
        }
    }


    @Override
    public void isConnected(boolean isConnect) {
        if (btDialog != null) {
            btDialog.dismiss();
        }
        menu.findItem(R.id.bluetooth).setIcon(isConnect ? R.drawable.ic_bluetooth_connect : R.drawable.ic_bluetooth_disconnect);

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
        utils.saveAppVerLastCheckedAt();
    }


}
