package cn.labelnet.bletooth.ble.bean;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.List;

/**
 * @Package cn.labelnet.bletooth.ble.bean
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 8:27 PM 2/9/2017
 * @Desc
 * BleCharacteristic bean
 */

public class BleCharacteristic {

    private String UUID;
    private BluetoothGattCharacteristic characteristic;
    private List<BluetoothGattDescriptor> descriptors;

    public BleCharacteristic(String UUID, BluetoothGattCharacteristic characteristic, List<BluetoothGattDescriptor> descriptors) {
        this.UUID = UUID;
        this.characteristic = characteristic;
        this.descriptors = descriptors;
    }

    public String getUUID() {
        return UUID;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public List<BluetoothGattDescriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BleCharacteristic)) return false;

        BleCharacteristic that = (BleCharacteristic) o;

        if (!UUID.equals(that.UUID)) return false;
        return characteristic.equals(that.characteristic);

    }

    @Override
    public int hashCode() {
        int result = UUID.hashCode();
        result = 31 * result + characteristic.hashCode();
        return result;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String UUID;
        private BluetoothGattCharacteristic characteristic;
        private List<BluetoothGattDescriptor> descriptors;

        public Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public Builder setCharacteristic(BluetoothGattCharacteristic characteristic) {
            this.characteristic = characteristic;
            return this;
        }

        public Builder setDescriptors(List<BluetoothGattDescriptor> descriptors) {
            this.descriptors = descriptors;
            return this;
        }

        public BleCharacteristic build() {
            return new BleCharacteristic(UUID, characteristic, descriptors);
        }

    }

}
