package cn.labelnet.bletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.labelnet.bletooth.async.AsyncCenter;
import cn.labelnet.bletooth.core.scan.BleBlueToothTest;
import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.core.conn.BleConnStatus;
import cn.labelnet.bletooth.core.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.core.scan.ble.BleToothBleScanCallback;
import cn.labelnet.bletooth.core.BleGattCallback;
import cn.labelnet.bletooth.core.BleScanCallBack;
import cn.labelnet.bletooth.core.simple.SimpleBleScanResultCallback;
import cn.labelnet.bletooth.core.simple.SimpleLeScanResultCallBack;
import cn.labelnet.bletooth.core.simple.SimpleScanAndConnCallBack;
import cn.labelnet.bletooth.core.scan.le.BleToothLeScanCallBack;
import cn.labelnet.bletooth.util.ClsBleUtil;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:58 PM 2/5/2017
 * @Desc
 *
 */

public class BleTooth implements BleToothBleScanCallback.OnScanCompleteListener
        , BleToothLeScanCallBack.OnScanCompleteListener
        , BleToothBleGattCallBack.OnConnStatusListener {

    private static final String TAG = BleBlueToothTest.class.getSimpleName();

    private Context mContext;

    //bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothDevice mBlueToothDevice;
    private BleDevice mBleDevice;
    //scan
    private BleToothBleScanCallback scanCallback;
    //scan  5.0 scanner
    private BluetoothLeScanner mBluetoothLeScaner;
    private BleToothLeScanCallBack leScanCallBack;
    //conn
    private int connCount = 0;
    private int timeOutCount = 0;
    private AtomicBoolean isAutoConn = new AtomicBoolean(false);
    private BleGattCallback gattCallBack;
    //control
    private AtomicBoolean isConnBle = new AtomicBoolean(false);
    private AsyncCenter mAsyncCenter;

    private static BleTooth mInstance;

    public static BleTooth getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BleBlueToothTest.class) {
                if (mInstance == null) {
                    mInstance = new BleTooth(context);
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
    public BleTooth(Context context) {
        this.mContext = context.getApplicationContext();
        mAsyncCenter = AsyncCenter.getInstance();
        initBlueTooth();
    }

    private void initBlueTooth() {
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (isBuildLOLLIPOP()) {
            mBluetoothLeScaner = mBluetoothAdapter.getBluetoothLeScanner();
        }
    }

    //==================================== SCAN ===================================================


    /**
     * start scan must be params is BleScanCallBack
     *
     * @param bleScanCallBack 5.0 and 4.3 scan callback
     */
    public void startScan(final BleScanCallBack bleScanCallBack) {

        if (bleScanCallBack == null) {
            throw new IllegalArgumentException("StartScan BleScanCallBack is Null");
        }

        if (isBuildLOLLIPOP()) {
            //5.0
            LogUtil.v("start scan use android 5.0 Le");
            leScanCallBack = new SimpleLeScanResultCallBack(bleScanCallBack);
            startScan(leScanCallBack);
        } else {
            LogUtil.v("start scan use android 4.3 BLe");
            scanCallback = new SimpleBleScanResultCallback(bleScanCallBack);
            startScan(scanCallback);
        }
    }

    /**
     * start scan
     *
     * @param callback LeCallback
     */
    private void startScan(final BleToothBleScanCallback callback) {
        this.scanCallback = callback;
        boolean startResult = mBluetoothAdapter.startLeScan(scanCallback);
        if (startResult) {
            callback.setOnScanCompleteListener(this);
            scanCallback.onStartTimmer();
        } else {
            stopScan(scanCallback);
        }
    }

    /**
     * android 5.0 scan
     * start scan ble
     * filter need override @see BleToothLeScanCallBack#getScanFilters() method
     * setting need override @see BleToothLeScanCallBack#getScanSettings(ScanSettings) method
     *
     * @param leScanCallBack android 5.0
     */
    private void startScan(final BleToothLeScanCallBack leScanCallBack) {
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

    /**
     * stop scan
     *
     * @param callback BleToothBleScanCallback
     */
    private void stopScan(BleToothBleScanCallback callback) {
        callback.onStopTimmer();
        mBluetoothAdapter.stopLeScan(callback);
    }


    /**
     * android 5.0
     * stop scan
     *
     * @param leScanCallBack
     */
    private void stopScan(final BleToothLeScanCallBack leScanCallBack) {
        if (mBluetoothLeScaner != null) {
            leScanCallBack.onStopTimmer();
            mBluetoothLeScaner.stopScan(leScanCallBack);
        }
    }

    @Override
    public void onScanFinish() {
        LogUtil.v("Over Scan Finish !");
        if (isBuildLOLLIPOP()) {
            if (leScanCallBack != null) {
                stopScan(leScanCallBack);
            }
        } else {
            if (scanCallback != null) {
                stopScan(scanCallback);
            }
        }
    }

    //==================================== CONN ===================================================

    /**
     * conn blue
     *
     * @param bleDevice             chip bean
     * @param isAutoConn            isAuto
     * @param bluetoothGattCallback callback
     */
    public synchronized void connect(BleDevice bleDevice, boolean isAutoConn, BleGattCallback bluetoothGattCallback) {
        if (bleDevice == null) {
            throw new IllegalArgumentException("BleDevice Bean is null!");
        }

        if (bluetoothGattCallback == null) {
            throw new IllegalArgumentException("BleToothBleGattCallBack is null!");
        }

        this.mBleDevice = bleDevice;
        this.mBlueToothDevice = bleDevice.getBluetoothDevice();
        this.isAutoConn.set(isAutoConn);
        this.gattCallBack = bluetoothGattCallback;
        bluetoothGattCallback.setOnConnStatusListener(this);
        mBluetoothGatt = mBlueToothDevice.connectGatt(mContext, isAutoConn, bluetoothGattCallback);
    }

    @Override
    public void onFail() {
        connCount++;
        if (connCount == 5) {
            gattCallBack.setBleConnStatus(BleConnStatus.fail);
            isConnBle.set(false);
            return;
        }
        disconnect();
        connect(mBleDevice, isAutoConn.get(), gattCallBack);
    }

    @Override
    public void onSuccess() {
        connCount = 0;
        timeOutCount = 0;
        isConnBle.set(true);
    }

    @Override
    public void onTimeOut() {
        timeOutCount++;
        if (timeOutCount == 5) {
            gattCallBack.setBleConnStatus(BleConnStatus.conntimeout);
            ClsBleUtil.cancelBondProcess(mBlueToothDevice);
            isConnBle.set(false);
            return;
        }
        disconnect();
        connect(mBleDevice, isAutoConn.get(), gattCallBack);
    }

    /**
     * disconnect, refresh and close bluetooth gatt.
     */
    public void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            ClsBleUtil.refreshDeviceCache(mBluetoothGatt);
            mBluetoothGatt.close();
        }
    }

    /**
     * is conn ble tooth
     *
     * @return
     */
    public boolean isConnBleTooth() {
        return isConnBle.get();
    }

    //==================================== Scan and Conn =========================================


    /**
     * scan conn
     *
     * @param mac                   mac address
     * @param bluetoothGattCallback conn callback
     * @param onScanAndConnListener scan listener
     */
    public void scanAndConnect(String mac
            , final BleGattCallback bluetoothGattCallback
            , SimpleScanAndConnCallBack.OnScanAndConnListener onScanAndConnListener) {
        scanAndConnect(mac, false, bluetoothGattCallback, onScanAndConnListener);
    }

    /**
     * scan conn
     *
     * @param mac                   mac address
     * @param isAutoConn            auto conn
     * @param bluetoothGattCallback conn callback
     * @param onScanAndConnListener scan listener
     */
    public void scanAndConnect(final String mac
            , final boolean isAutoConn
            , final BleGattCallback bluetoothGattCallback
            , SimpleScanAndConnCallBack.OnScanAndConnListener onScanAndConnListener) {

        if (mac == null || mac.split(":").length != 6) {
            throw new IllegalArgumentException("MAC is null or error! ");
        }
        this.gattCallBack = bluetoothGattCallback;
        SimpleScanAndConnCallBack scanAndConnCallBack = new SimpleScanAndConnCallBack(mac) {
            @Override
            protected void onConnect(BleDevice bleDevice) {
                mBleDevice = bleDevice;
                mBlueToothDevice = bleDevice.getBluetoothDevice();
                connect(bleDevice, isAutoConn, bluetoothGattCallback);
            }
        };
        scanAndConnCallBack.setOnScanAndConnListener(onScanAndConnListener);
        startScan(scanAndConnCallBack);
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public BleDevice getBleDevice() {
        return mBleDevice;
    }

    public Context getContext() {
        return mContext;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    /**
     * support Android L 5.0
     *
     * @return is support package android.bluetooth.le
     */
    private boolean isBuildLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * ======================================= Operation：read , write notify =====================
     */




}
