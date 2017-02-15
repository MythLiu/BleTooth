package cn.labelnet.bletooth_peripheral;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Build;

import cn.labelnet.bletooth_peripheral.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth_peripheral
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:09 PM 2/15/2017
 * @Desc Desc
 * Android Ble Peripheral
 * (1) Android 5.0 support BlueTooth Ble Peripheral，so System Version must be >= 21
 * (2)
 */

public class BlePeripheral {


    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private Context mContext;

    /**
     * ble peripheral
     *
     * @param applicationContext application context
     * @param isShowLog          true : show log
     */
    public BlePeripheral(Context applicationContext, boolean isShowLog) {
        this.mContext = applicationContext;
        // init log
        LogUtil.init(isShowLog);
        initBle();
    }

    private void initBle() {
        if (isBuildLOLLIPOP()) {
            throw new IllegalStateException("Your android system version too low , must be >= 21 (Android 5.0)");
        }

        if (!isCheckSupportPerpheral()) {
            try {
                throw new Exception("Your Android Device not support ble peripheral!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        if (mBluetoothLeAdvertiser == null) {
            try {
                throw new Exception("Your Android Device not support ble peripheral , BluetoothLeAdvertiser is null !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * System VERSION
     *
     * @return API is >=21
     */
    private boolean isBuildLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
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

}
