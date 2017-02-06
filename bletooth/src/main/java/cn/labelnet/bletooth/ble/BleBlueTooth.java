package cn.labelnet.bletooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import cn.labelnet.bletooth.ble.scan.BleToothBleScanCallback;

/**
 * @Package cn.labelnet.bletooth
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:27 PM 2/5/2017
 * @Desc BleTooth
 * (1) 只允许一个BluetoothGattCallback
 * (2)
 */

public class BleBlueTooth {

    private static final String TAG = BleBlueTooth.class.getSimpleName();

    private Context mContext;

    //bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;

    //control
    private Handler handler = new Handler(Looper.getMainLooper());


    /**
     * init bluetooth
     *
     * @param context Application Context
     */
    public BleBlueTooth(Context context) {
        this.mContext = context.getApplicationContext();
        initBlueTooth();
    }

    private void initBlueTooth() {
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

    }

    /**
     * start scan
     *
     * @param callback LeCallback
     */
    public void startScan(BleToothBleScanCallback callback) {
        boolean startResult = mBluetoothAdapter.startLeScan(callback);
        if (startResult) {
            callback.onStartTimmer();
        } else {
            callback.onStopTimmer();
        }
    }

    /**
     * stop scan
     * @param callback
     */
    public void stopScan(BleToothBleScanCallback callback){
        callback.onStopTimmer();
        mBluetoothAdapter.stopLeScan(callback);
    }


}
