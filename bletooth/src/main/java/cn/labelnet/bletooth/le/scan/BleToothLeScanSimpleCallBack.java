package cn.labelnet.bletooth.le.scan;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.ble.bean.BleDevice;


/**
 * @Package cn.labelnet.bletooth.le.scan
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:14 PM 2/7/2017
 * @Desc Desc
 * 蓝牙扫描，演示列子
 */

public class BleToothLeScanSimpleCallBack extends LeScanResultCallBack {
    public BleToothLeScanSimpleCallBack(long timeOutMill) {
        super(timeOutMill);
    }

    @Override
    protected void onScanDevicesData(List<BleDevice> bleDevices) {
        //结果
    }

    @Override
    protected void onNotifyBleToothDeviceRssi(int position, int rssi) {
        // 更新rssi
    }

    @Override
    protected void onScanStatus(int errorCode) {
        //状态
    }

    @Override
    protected void bleToothScanProcess(float process) {
        //进度
    }

    @Override
    public List<ScanFilter> getScanFilters() {
        //过滤
        List<ScanFilter> scanFilters = new ArrayList<>();
        ScanFilter filter1 = new ScanFilter
                .Builder()
                .setDeviceName("SimpleBLEPeripheral")
                .build();
        scanFilters.add(filter1);
        return scanFilters;
    }

    @Override
    public ScanSettings getScanSettings(ScanSettings.Builder builder) {
        //配置
        return super.getScanSettings(builder);
    }
}
