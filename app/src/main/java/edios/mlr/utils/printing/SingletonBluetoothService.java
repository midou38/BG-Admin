package edios.mlr.utils.printing;

import android.content.Context;
import android.os.Handler;

import edios.mlr.interfaces.BluetoothConnectivity;

public class SingletonBluetoothService{
    private static BluetoothService mService;
    private static SingletonBluetoothService singletonBluetoothService=null;
    private  static Handler myHandler;

   /* private SingletonBluetoothService(Context context) {
        this.context=context;
        myHandler=new MyHandler(context);

    }*/

    private SingletonBluetoothService(Context context, BluetoothConnectivity btConnect) {
        myHandler=new MyHandler(context,btConnect);
    }

    public static BluetoothService getInstance(Context context, BluetoothConnectivity btConnect){
        if(mService==null) {
            singletonBluetoothService=new SingletonBluetoothService(context,btConnect);
            mService = new BluetoothService(context, myHandler);
        }
        return mService;
    }
}