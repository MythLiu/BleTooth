package cn.labelnet.bletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import cn.labelnet.bletooth.scan.BleToothScanCallback;

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

public class BleTooth {

    private static final String TAG = BleTooth.class.getSimpleName();

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
    public BleTooth(Context context) {
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
    public void startScan(BleToothScanCallback callback) {
        callback.onStartScan();
//        mBluetoothAdapter.startLeScan()
//        mBluetoothAdapter.startScan()
//        Build.VERSION_CODES
    }


}
