package cn.labelnet.bletooth.le.scan;

import android.bluetooth.le.ScanResult;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.le.scan
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:32 PM 2/7/2017
 * @Desc Desc
 * scan result
 */

public abstract class LeScanResultCallBack extends BleToothLeScanCallBack {

    private List<BleDevice> bleDevices;

    public LeScanResultCallBack(long timeOutMillis) {
        super(timeOutMillis);
        bleDevices = new ArrayList<>();
    }

    protected void addDevice(BleDevice bleDevice, int rssi) {
        if (bleDevices.contains(bleDevice)) {
            int position = bleDevices.indexOf(bleDevice);
            onNotifyBleToothDeviceRssi(position, rssi);
            return;
        }
        bleDevices.add(bleDevice);
        onScanDevicesData(bleDevices);
    }

    /**
     * scan result
     * @param bleDevices
     */
    protected abstract void onScanDevicesData(List<BleDevice> bleDevices);

    // update device rssi
    protected abstract void onNotifyBleToothDeviceRssi(int position, int rssi);


    @Override
    public void onStartTimmer() {
        super.onStartTimmer();
        onScanDevicesData(bleDevices);
    }

    @Override
    protected void onCheckStatus() {
        if (bleDevices == null || bleDevices.size() == 0) {
            onScanStatus(LeScanStatus.SCAN_TIMEOUT);
            return;
        }
        onScanStatus(LeScanStatus.SCAN_END);
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        LogUtil.v("DeviceName : " + result.getDevice().getName() + " DeviceMac: " + result.getDevice().getAddress());
        BleDevice bleDevice = new BleDevice();
        bleDevice.setBluetoothDevice(result.getDevice());
        bleDevice.setDeviceName(result.getDevice().getName());
        bleDevice.setDeviceMac(result.getDevice().getAddress());
        bleDevice.setRssi(result.getRssi());
        addDevice(bleDevice, result.getRssi());
    }

    @Deprecated
    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        super.onBatchScanResults(results);
        // !!! WAINING : this method don't data
        // Deprecated
    }
}