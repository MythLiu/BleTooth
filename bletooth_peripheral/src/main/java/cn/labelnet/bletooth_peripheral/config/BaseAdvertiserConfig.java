package cn.labelnet.bletooth_peripheral.config;

import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;

/**
 * @Package cn.labelnet.bletooth_peripheral.config
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:44 PM 2/15/2017
 * @Desc Desc
 */

public abstract class BaseAdvertiserConfig {

    /**
     * Advertise Settings ： 广告设置
     * (1) power mode ：功耗 : low , balance , high
     * (2) (TX) power level ：信号强度 : ultra , low , medium , high
     * (3) Connectable : 可连接性
     * (4) timeoutmills ： 超时时间
     */
    private AdvertiseSettings mAdvertiseSettings;


    /**
     * AdvertiseData ： 广告数据包，设置要向外广播的出的内容
     * (1) max data length : 31bytes
     * (2) setIncludeTxPowerLevel ： rssi 信号强度
     * (3) setIncludeDeviceName : 设备名称
     * (4) addManufacturerData() : 添加厂商特定数据 Bluetooth SIG
     * (5) addServiceData() : 添加Gatt Service UUID and byte data
     */
    private AdvertiseData mAdvertiseData;

}
