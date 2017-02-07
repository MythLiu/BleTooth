package cn.labelnet.bletooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;

import java.util.List;

import cn.labelnet.bletooth.ble.BleBlueTooth;
import cn.labelnet.bletooth.le.scan.BleToothLeScanCallBack;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.le
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 9:51 AM 2/6/2017
 * @Desc Desc
 */

public class LeBlueTooth implements BleToothLeScanCallBack.OnScanCompleteListener {

    private Context mContext;

    //bluetooth
    private BluetoothAdapter mBluetoothAdapter;

    //scan
    private BluetoothLeScanner mBluetoothLeScaner;
    private BleToothLeScanCallBack leScanCallBack;

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

    /**
     * start scan ble
     * filter need override @see BleToothLeScanCallBack#getScanFilters() method
     * setting need override @see BleToothLeScanCallBack#getScanSettings(ScanSettings) method
     *
     * @param leScanCallBack result
     */
    public void startScan(final BleToothLeScanCallBack leScanCallBack) {
        if (leScanCallBack == null) {
            throw new IllegalArgumentException("start Scan callback is null");
        }
        this.leScanCallBack = leScanCallBack;
        startScan(leScanCallBack.getScanFilters()
                , leScanCallBack.getScanSettings(new ScanSettings.Builder())
                , leScanCallBack);
        leScanCallBack.setOnScanCompleteListener(this);
        leScanCallBack.onStartTimmer();
    }


    private void startScan(List<ScanFilter> filters, ScanSettings settings,
                           final ScanCallback callback) {
        if (mBluetoothLeScaner != null) {
            mBluetoothLeScaner.startScan(filters, settings, callback);
        } else {
            throw new IllegalArgumentException("Don't Support BLE BlueToothLeScanner");
        }
    }

    @Override
    public void onScanFinish() {
        LogUtil.v("LeBlueTooth Scan Finish");
        stopScan(leScanCallBack);
    }

    /**
     * stop scan
     *
     * @param leScanCallBack
     */
    public void stopScan(final BleToothLeScanCallBack leScanCallBack) {
        if (mBluetoothLeScaner != null) {
            leScanCallBack.onStopTimmer();
            mBluetoothLeScaner.stopScan(leScanCallBack);
        }
    }



}
