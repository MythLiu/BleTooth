package cn.labelnet.bletooth.ble.scan;

import android.bluetooth.BluetoothDevice;

import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.ble.scan
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:29 PM 2/6/2017
 * @Desc Desc
 * According to device mac filter Ble
 */

public abstract class BleScanResultMacCallback extends BleScanResultCallback {

    private String devicesMac;

    public BleScanResultMacCallback(long timeOutMillis, String devicesMac) {
        super(timeOutMillis);
        this.devicesMac = devicesMac;
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        LogUtil.v("Name : " + device.getName() + " | MAC : " + device.getAddress() + " | RSSI　：　" + rssi);
        String deviceName = device.getName();
        String deviceMac = device.getAddress();
        if (deviceName != null && deviceName.trim().length() > 0) {
            if (devicesMac.equals(deviceMac)) {
                BleDevice bleDevice = new BleDevice();
                bleDevice.setBluetoothDevice(device);
                bleDevice.setDeviceName(deviceName);
                bleDevice.setDeviceMac(deviceMac);
                bleDevice.setRssi(rssi);
                addDevice(bleDevice, rssi);
            }
        }
    }
}
