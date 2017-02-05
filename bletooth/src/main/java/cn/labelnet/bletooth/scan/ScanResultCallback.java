package cn.labelnet.bletooth.scan;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.bean.BleDevice;
import cn.labelnet.util.LogUtil;

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

    private List<BleDevice> bleDevices;

    public ScanResultCallback(long timeOutMillis) {
        setTimeOutMillis(timeOutMillis);
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

    @Override
    public List<BleDevice> getBleDevices() {
        return bleDevices;
    }
}
