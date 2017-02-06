package cn.labelnet.bletooth.ble.conn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.ble.conn
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:08 PM 2/6/2017
 * @Desc Desc
 * conn bluetooth call back
 * (1)conn status
 * (2)control
 */

public abstract class BleToothBleGattCallBack extends BluetoothGattCallback {

    private OnConnStatusListener onConnStatusListener;

    public void setOnConnStatusListener(OnConnStatusListener onConnStatusListener) {
        this.onConnStatusListener = onConnStatusListener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        LogUtil.v("Status : " + status + "　NewState : " + newState);

        if (status == 133 && newState == 0) {
            onConnStatusListener.onTimeOut();
            return;
        }
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            setBleConnStatus(BleConnStatus.success);
            onConnStatusListener.onSuccess();
            gatt.discoverServices();
        } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            onConnStatusListener.onFail();
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

    public abstract void setBleConnStatus(BleConnStatus status);

    //conn listener
    public interface OnConnStatusListener {

        void onFail();

        void onSuccess();

        void onTimeOut();
    }

}
