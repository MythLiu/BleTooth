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

    private void addDevice(BleDevice bleDevice, int rssi) {
        if (bleDevices.contains(bleDevice)) {
            int position = bleDevices.indexOf(bleDevice);
            onNotifyBleToothDeviceRssi(position, rssi);
            return;
        }
        bleDevices.add(bleDevice);
        onScanDevicesData(bleDevices);
    }


    protected abstract void onNotifyBleToothDeviceRssi(int position, int rssi);

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        LogUtil.v("Name : " + device.getName() + " | MAC : "+device.getAddress() + " | RSSI　：　" + rssi);
        String deviceName = device.getName();
        String deviceMac = device.getAddress();
        if (deviceName != null && deviceName.trim().length() > 0) {
            //chip Name can use !
            if (isScanDeviceNameFilter(deviceName)) {
                BleDevice bleDevice = new BleDevice();
                bleDevice.setBluetoothDevice(device);
                bleDevice.setDeviceName(deviceName);
                bleDevice.setDeviceMac(deviceMac);
                bleDevice.setRssi(rssi);
                addDevice(bleDevice, rssi);
            }

        }
    }

    /**
     * filter
     *
     * @param deviceName chip Name
     * @return is need chip Name
     */
    private boolean isScanDeviceNameFilter(String deviceName) {
        List<String> scanFilter = getScanFilter();

        // no filter
        if (scanFilter == null || scanFilter.size() == 0) {
            return true;
        }

        // have filter
        for (String filter : scanFilter) {
            if (deviceName.startsWith(filter) || deviceName.endsWith(filter) || deviceName.contains(filter)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onScanFinish() {
        if (bleDevices.size() == 0) {
            setBleToothScanStatus(BleScanStatus.timeout);
            return;
        }
        setBleToothScanStatus(BleScanStatus.disscan);
    }

    @Override
    protected void onScanStart() {
        onScanDevicesData(bleDevices);
    }

    /**
     * devices info filter
     *
     * @return filter
     */
    protected List<String> getScanFilter() {
        return null;
    }

    // scan result
    protected abstract void onScanDevicesData(List<BleDevice> bleDevices);

}
