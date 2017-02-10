package cn.labelnet.bletooth.ble.conn;

import cn.labelnet.bletooth.data.filter.BleBluetoothUUIDFilter;

/**
 * @Package cn.labelnet.bletooth.ble.conn
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 1:11 PM 2/10/2017
 * @Desc example filter bluetooth gatt service
 */

public class SimpleBleToothBleFilterGattCallback extends BleToothBleFilterGattCallBack {

    @Override
    protected BleBluetoothUUIDFilter onFilterBluetoothGattService(BleBluetoothUUIDFilter.Builder builder) {
        builder.startBuilderService()
                .addService("f000c0e0-0451-4000-b000-000000000000")
                .addCharacteristic("f000c0e1-0451-4000-b000-000000000000", "00002902-0000-1000-8000-00805f9b34fb", "00002901-0000-1000-8000-00805f9b34fb")
                .addCharacteristic("f000c0e2-0451-4000-b000-000000000000", "00002901-0000-1000-8000-00805f9b34fb")
                .endBuilderService();
        return builder.build();
    }

    @Override
    public void setBleConnStatus(BleConnStatus status) {

    }
}
