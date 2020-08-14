package edios.mlr.utils.printing;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

import edios.mlr.R;


public class DeviceListActivity extends AppCompatActivity {
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private final BroadcastReceiver mReceiver = new C00332();
    BluetoothService mService = null;
    private OnItemClickListener mDeviceClickListener = new C00321();
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setResult(0);
        setTitle("Connect Printer");
        findViewById(R.id.button_scan).setOnClickListener(new C00343());
        this.mPairedDevicesArrayAdapter = new ArrayAdapter(this, R.layout.device_name);
        this.mNewDevicesArrayAdapter = new ArrayAdapter(this, R.layout.device_name);
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(this.mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(this.mDeviceClickListener);
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(this.mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(this.mDeviceClickListener);
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        this.mService = new BluetoothService(this, null);
        Set<BluetoothDevice> pairedDevices = this.mService.getPairedDev();
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                this.mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            return;
        }
        this.mPairedDevicesArrayAdapter.add("None Paired");
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mService != null) {
            this.mService.cancelDiscovery();
        }
        this.mService = null;
        unregisterReceiver(this.mReceiver);
    }

    private void doDiscovery() {
        setProgressBarIndeterminateVisibility(false);
        setTitle("Scanning...");
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        if (this.mService.isDiscovering()) {
            this.mService.cancelDiscovery();
        }
        this.mService.startDiscovery();
    }

    class C00321 implements OnItemClickListener {
        C00321() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            System.out.println("C00321.onItemClick");
            DeviceListActivity.this.mService.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent intent = new Intent();
            intent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);
            Log.d("device address", address);
            DeviceListActivity.this.setResult(-1, intent);
            DeviceListActivity.this.finish();
        }
    }

    class C00332 extends BroadcastReceiver {
        C00332() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("C00332.onReceive "+action );
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (device.getBondState() != 12) {
                    DeviceListActivity.this.mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                DeviceListActivity.this.setProgressBarIndeterminateVisibility(false);
                DeviceListActivity.this.setTitle("Select Device");
                if (DeviceListActivity.this.mNewDevicesArrayAdapter.getCount() == 0) {
                    DeviceListActivity.this.mNewDevicesArrayAdapter.add("No device found");
                }
            }
        }
    }

    class C00343 implements OnClickListener {
        C00343() {
        }

        public void onClick(View v) {
            DeviceListActivity.this.doDiscovery();
            v.setVisibility(View.GONE);
        }
    }
}
