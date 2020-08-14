package edios.mlr.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import edios.mlr.interfaces.NotifyFetchOrder;
import edios.mlr.model.ResultOutput;
import edios.mlr.utils.AppConstants;
import edios.mlr.utils.SharedPref;
import edios.mlr.utils.Utils;

public class FetchOrderService extends Service implements NotifyFetchOrder {
    //    private final int UPDATE_INTERVAL = 15 * 1000;
    private final int UPDATE_INTERVAL = SharedPref.getReader(this).getInt(AppConstants.ORDER_REFRESH_TIME, AppConstants.DEFAULT_REFRESH_TIME) * 1000;
    private Timer timer = new Timer();

    public FetchOrderService() {
    }

    @Override
    public void onCreate() {
        System.out.println("FetchOrderService.onCreate");
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startid) {
        System.out.println("FetchOrderService.onStartCommand " + UPDATE_INTERVAL);
        final Utils utils = new Utils(this);
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                utils.fetchOrders(FetchOrderService.this, "","");
            }
        }, UPDATE_INTERVAL, UPDATE_INTERVAL);
        return START_STICKY;
    }

    private void stopService() {
        if (timer != null) timer.cancel();
    }

    @Override
    public void orderFetched(ResultOutput orders, boolean isSuccess) {
//        SimpleDateFormat sdf3 = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH);
        System.out.println("FetchOrderService.orderFetched");
        if (isSuccess) {
            System.out.println("FetchOrderService.orderFetched if");
            sendBroadcast(new Intent(AppConstants.UPDATE_ORDERS).putExtra(AppConstants.UPDATE_ORDERS,orders));
        } else {
            System.out.println("FetchOrderService.orderFetched else");
        }
    }


}
