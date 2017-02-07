package cn.labelnet.bletooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.labelnet.bletooth.ble.BleBlueTooth;
import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.ble.conn.BleConnStatus;
import cn.labelnet.bletooth.ble.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.le.scan.BleToothLeScanCallBack;
import cn.labelnet.bletooth.util.ClsBleUtil;
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

public class LeBlueTooth implements BleToothLeScanCallBack.OnScanCompleteListener
        , BleToothBleGattCallBack.OnConnStatusListener {

    private static final UUID UUID_SERVER = UUID.fromString("");
    private static final UUID UUID_CHARREAD = UUID.fromString("");
    private static final UUID UUID_DESCRIPTOR = UUID.fromString("");
    private static final UUID UUID_CHARWRITE = UUID.fromString("");
    private Context mContext;

    //bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;

    //scan
    private BluetoothLeScanner mBluetoothLeScaner;
    private BleToothLeScanCallBack leScanCallBack;

    //conn
    private BluetoothDevice mBlueToothDevice;
    private BleDevice mBleDevice;
    private int connCount = 0;
    private int timeOutCount = 0;
    private AtomicBoolean isAutoConn = new AtomicBoolean(false);
    private BleToothBleGattCallBack gattCallBack;
    private AtomicBoolean isConnBle = new AtomicBoolean(false);

    /**
     * use BluetoothGattServer init BluetoothGatt
     */
    private BluetoothGattServer mBluetoothGattServer;

    //Advertiser
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
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
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

    //========================================== CONN ==============================================

    /**
     *
     */
    /**
     * conn blue
     *
     * @param bleDevice             chip bean
     * @param isAutoConn            isAuto
     * @param bluetoothGattCallback callback
     */
    public synchronized void connect(BleDevice bleDevice, boolean isAutoConn, BleToothBleGattCallBack bluetoothGattCallback) {
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

    public BluetoothGatt getmBluetoothGatt() {
        return mBluetoothGatt;
    }

    public BleDevice getmBleDevice() {
        return mBleDevice;
    }

    public Context getmContext() {
        return mContext;
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }


//========================================== CONN Advertiser Init GattServer==============


    private void initGATTServer() {
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setConnectable(true)
                .build();

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(true)
                .build();

        AdvertiseData scanResponseData = new AdvertiseData.Builder()
                .addServiceUuid(new ParcelUuid(UUID_SERVER))
                .setIncludeTxPowerLevel(true)
                .build();


        AdvertiseCallback callback = new AdvertiseCallback() {

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                LogUtil.v("BLE advertisement added successfully : initGATTServer success");
                initServices();
            }

            @Override
            public void onStartFailure(int errorCode) {
                LogUtil.e("Failed to add BLE advertisement, reason: " + errorCode);
            }
        };

        BluetoothLeAdvertiser bluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        bluetoothLeAdvertiser.startAdvertising(settings, advertiseData, scanResponseData, callback);
    }

    private void initServices() {
        mBluetoothGattServer = mBluetoothManager.openGattServer(mContext, bluetoothGattServerCallback);
        BluetoothGattService service = new BluetoothGattService(UUID_SERVER, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        //add a read characteristic.
        BluetoothGattCharacteristic characteristicRead = new BluetoothGattCharacteristic(UUID_CHARREAD, BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);
        //add a descriptor
        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(UUID_DESCRIPTOR, BluetoothGattCharacteristic.PERMISSION_WRITE);
        characteristicRead.addDescriptor(descriptor);

        service.addCharacteristic(characteristicRead);

        //add a write characteristic.
        BluetoothGattCharacteristic characteristicWrite = new BluetoothGattCharacteristic(UUID_CHARWRITE,
                BluetoothGattCharacteristic.PROPERTY_WRITE |
                        BluetoothGattCharacteristic.PROPERTY_READ |
                        BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        service.addCharacteristic(characteristicWrite);

        mBluetoothGattServer.addService(service);
        LogUtil.v("initServices ok");
    }

    private BluetoothGattServerCallback bluetoothGattServerCallback = new BluetoothGattServerCallback() {
    };

    private void stopAdvertise() {
        if (mBluetoothLeAdvertiser != null) {
//            mBluetoothLeAdvertiser.stopAdvertising(m);
            mBluetoothLeAdvertiser = null;
        }
    }

}
