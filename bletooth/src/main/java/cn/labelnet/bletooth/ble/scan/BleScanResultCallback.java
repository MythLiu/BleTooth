package cn.labelnet.bletooth.ble.scan;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.ble.scan
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:04 PM 2/5/2017
 * @Desc (1) scan result
 * (2) scan filter
 */

public abstract class BleScanResultCallback extends BleToothBleScanCallback {

    private List<BleDevice> bleDevices;

    public BleScanResultCallback(long timeOutMillis) {
        super(timeOutMillis);
        bleDevices = new ArrayList<>();
    }

    public void addDevice() {
        //filter
    }

    public void notifyRssi() {
        onNotifyBleToothDeviceRssi(1, 122);
    }


    protected abstract void onNotifyBleToothDeviceRssi(int position, int rssi);

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        LogUtil.v("Name : " + device.getName() + " | MAC : "+device.getAddress() + " | RSSI　：　" + rssi);
    }

    public List<BleDevice> getBleDevices() {
        return bleDevices;
    }

    @Override
    protected void onScanFinish() {

        onScanComplete(getBleDevices());
    }

    // scan complete
    protected abstract void onScanComplete(List<BleDevice> bleDevices);

}
