package cn.labelnet.bletooth.ble.conn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

/**
 * @Package cn.labelnet.bletooth.ble.conn
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:08 PM 2/6/2017
 * @Desc Desc
 */

public abstract class BleToothBleGattCallBack extends BluetoothGattCallback {

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            setBleConnStatus(BleConnStatus.success);
            gatt.discoverServices();
        } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            setBleConnStatus(BleConnStatus.fail);
        } else if (newState == BluetoothGatt.STATE_CONNECTING) {
            setBleConnStatus(BleConnStatus.conning);
        } else if (newState == BluetoothGatt.STATE_DISCONNECTING) {
            setBleConnStatus(BleConnStatus.disconning);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            setBleConnStatus(BleConnStatus.gatt_success);
        } else {
            setBleConnStatus(BleConnStatus.gatt_fail);
        }
    }

    protected abstract void setBleConnStatus(BleConnStatus status);

}
