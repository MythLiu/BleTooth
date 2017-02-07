package cn.labelnet.bletooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Build;

import cn.labelnet.bletooth.ble.BleBlueTooth;

/**
 * @Package cn.labelnet.bletooth.le
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 9:51 AM 2/6/2017
 * @Desc Desc
 */

public class LeBlueTooth {

    private Context mContext;

    //bluetooth
    private BluetoothAdapter mBluetoothAdapter;

    //scan
    private BluetoothLeScanner mBluetoothLeScaner;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;


    private static LeBlueTooth mInstance;

    /**
     * init
     *
     * @param context applicationContext
     * @return instance
     */
    public static LeBlueTooth getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BleBlueTooth.class) {
                if (mInstance == null) {
                    mInstance = new LeBlueTooth(context);
                }
            }
        }
        return mInstance;
    }

    public LeBlueTooth(Context context) {
        this.mContext = context.getApplicationContext();
        initBlueTooth();
    }

    /**
     * System VERSION
     *
     * @return API is >=21
     */
    private boolean isBuildLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private void initBlueTooth() {
        final BluetoothManager mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (isBuildLOLLIPOP()) {
            mBluetoothLeScaner = mBluetoothAdapter.getBluetoothLeScanner();
            mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        }
    }

    /**
     * Support BLE Perpheral
     *
     * @return is support
     */
    public boolean isCheckSupportPerpheral() {
        if (isBuildLOLLIPOP()) {
            return mBluetoothAdapter.isMultipleAdvertisementSupported();
        }
        return false;
    }

    //========================================== SCAN ==============================================




}
