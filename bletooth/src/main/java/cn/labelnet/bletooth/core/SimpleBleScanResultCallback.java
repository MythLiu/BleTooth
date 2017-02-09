package cn.labelnet.bletooth.core;

import java.util.List;

import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.ble.scan.BleScanResultCallback;
import cn.labelnet.bletooth.ble.scan.BleScanStatus;

/**
 * @Package cn.labelnet.bletooth.core
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:12 PM 2/9/2017
 * @Desc Desc
 */

public class SimpleBleScanResultCallback extends BleScanResultCallback {

    private BleScanCallBack mBleScanCallBack;

    public SimpleBleScanResultCallback(BleScanCallBack bleScanCallBack) {
        super(bleScanCallBack.getTimeoutmills());
        this.mBleScanCallBack = bleScanCallBack;
    }

    @Override
    protected void onNotifyBleToothDeviceRssi(int position, int rssi) {
        mBleScanCallBack.onNotifyBleToothDeviceRssi(position, rssi);
    }

    @Override
    protected void onScanDevicesData(List<BleDevice> bleDevices) {
        mBleScanCallBack.onScanDevicesData(bleDevices);
    }

    @Override
    public void setBleToothScanStatus(BleScanStatus status) {
        mBleScanCallBack.onScanStatus(status);
    }

    @Override
    protected void bleToothScanProcess(float process) {
        mBleScanCallBack.onScanProcess(process);
    }

    @Override
    protected List<BleScanFilter> getScanFilter() {
        return mBleScanCallBack.getScanFilter();
    }
}