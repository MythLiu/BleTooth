package cn.labelnet.bletooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.ble.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.ble.scan.BleToothBleScanCallback;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.ble
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

    private static BleBlueTooth mInstance;

    public static BleBlueTooth getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BleBlueTooth.class) {
                if (mInstance == null) {
                    mInstance = new BleBlueTooth(context);
                }
            }
        }
        return mInstance;
    }


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

    //==================================== SCAN ===================================================
    /**
     * start scan
     *
     * @param callback LeCallback
     */
    public void startScan(final BleToothBleScanCallback callback) {
        boolean startResult = mBluetoothAdapter.startLeScan(callback);
        if (startResult) {
            callback.setOnScanCompleteListener(new BleToothBleScanCallback.OnScanCompleteListener() {
                @Override
                public void onScanFinish() {
                    LogUtil.v("Over Scan Finish !");
                    stopScan(callback);
                }
            });
            callback.onStartTimmer();
        } else {
            stopScan(callback);
        }
    }

    /**
     * stop scan
     * @param callback
     */
    public void stopScan(BleToothBleScanCallback callback) {
        callback.onStopTimmer();
        mBluetoothAdapter.stopLeScan(callback);
    }

    //==================================== CONN ===================================================

    /**
     * conn blue
     *
     * @param bleDevice             chip bean
     * @param isAutoConn            isAuto
     * @param bluetoothGattCallback callback
     */
    public void connect(BleDevice bleDevice, boolean isAutoConn, BleToothBleGattCallBack bluetoothGattCallback) {
        if (bleDevice == null) {
            throw new IllegalArgumentException("BleDevice Bean is null!");
        }

        if (bluetoothGattCallback == null) {
            throw new IllegalArgumentException("BleToothBleGattCallBack is null!");
        }
        BluetoothDevice bluetoothDevice = bleDevice.getBluetoothDevice();
        mBluetoothGatt = bluetoothDevice.connectGatt(mContext, isAutoConn, bluetoothGattCallback);
    }

}
