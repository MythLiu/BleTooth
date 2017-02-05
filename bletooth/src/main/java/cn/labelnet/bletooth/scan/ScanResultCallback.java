package cn.labelnet.bletooth.scan;

import android.bluetooth.BluetoothDevice;

/**
 * @Package cn.labelnet.bletooth.scan
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:04 PM 2/5/2017
 * @Desc (1) scan result
 * (2) scan filter
 */

public abstract class ScanResultCallback extends BleToothScanCallback {

    public ScanResultCallback(long timeOutMillis) {
        setTimeOutMillis(timeOutMillis);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

    }
}
