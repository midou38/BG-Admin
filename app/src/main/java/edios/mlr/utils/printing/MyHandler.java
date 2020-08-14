package edios.mlr.utils.printing;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import edios.mlr.interfaces.BluetoothConnectivity;

public class MyHandler extends Handler {
    Context activity;
    BluetoothConnectivity btConnect;
    public MyHandler(Context activity,BluetoothConnectivity btConnect) {
        this.activity=activity;
        this.btConnect=btConnect;
     }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                switch (msg.arg1) {
                    case 0:
                    case 1:
                        Log.d("蓝牙调试", "等待连接..... even i dont know what does it mean");
                        return;
                    case 2:
                        Log.d("蓝牙调试", "正在连接..... even i dont know what does it mean");
                        return;
                    case 3:
                        Toast.makeText(activity, "Connect successful", Toast.LENGTH_LONG).show();
                        btConnect.isConnected(true);
//                            PrintDemo.this.btnClose.setEnabled(true);
//                            PrintDemo.this.btnSend.setEnabled(true);
//                            PrintDemo.this.btnSendDraw.setEnabled(true);
                        return;
                    default:
                        return;
                }
            case 5:
                btConnect.isConnected(false);
                Toast.makeText(activity, "Device connection was lost", Toast.LENGTH_LONG).show();
                return;
            case 6:
                Toast.makeText(activity, "Unable to connect device", Toast.LENGTH_LONG).show();
                btConnect.isConnected(false);
                return;
            default:
                return;
        }
    }
}