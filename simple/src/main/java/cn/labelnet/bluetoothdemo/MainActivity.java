package cn.labelnet.bluetoothdemo;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.labelnet.bletooth.ble.BleBlueTooth;
import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.ble.conn.BleConnStatus;
import cn.labelnet.bletooth.ble.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.ble.scan.BleScanResultCallback;
import cn.labelnet.bletooth.ble.scan.BleScanStatus;
import cn.labelnet.bletooth.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    private BleBlueTooth bleTooth;
    private ScanCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleTooth = new BleBlueTooth(getApplicationContext());
        callBack = new ScanCallBack(5000);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleTooth.startScan(callBack);
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleTooth.stopScan(callBack);
            }
        });

        findViewById(R.id.btn_conn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BleDevice bleDevice = callBack.getBleDevice();
                if (bleDevice == null) {
                    LogUtil.v("没有设备");
                    return;
                }
                bleTooth.connect(bleDevice, false, new ConnCallBack());
            }
        });

        findViewById(R.id.btn_conn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleTooth.disconnect();
            }
        });

        findViewById(R.id.btn_scan_and_conn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleTooth.scanAndConnect("05:04:03:02:01:00", false, new ConnCallBack());
            }
        });

    }

    private static class ScanCallBack extends BleScanResultCallback {

        private List<BleDevice> bleDevices;

        public ScanCallBack(long timeOutMillis) {
            super(timeOutMillis);
        }

        public BleDevice getBleDevice() {
            if (bleDevices.size() > 0) {
                return bleDevices.get(0);
            } else {
                return null;
            }
        }

        @Override
        protected void onNotifyBleToothDeviceRssi(int position, int rssi) {
            LogUtil.v("----------------------------------------------------Position : " + position + " Rssi : " + rssi);
        }

        @Override
        protected void onScanDevicesData(List<BleDevice> bleDevices) {
            this.bleDevices = bleDevices;
            LogUtil.v("=================================================== BleDevices : " + bleDevices);
        }

        @Override
        public void setBleToothScanStatus(BleScanStatus status) {
            LogUtil.v("当前状态 ： " + status);
        }

        @Override
        protected void bleToothScanProcess(float process) {
            LogUtil.v("Process : " + process);
        }

        @Override
        protected List<String> getScanFilter() {
            List<String> filterList = new ArrayList<>();
//            filterList.add("CC2650");
            filterList.add("SimpleBLEPeripheral");
            return filterList;
        }
    }


    private static class ConnCallBack extends BleToothBleGattCallBack {

        @Override
        public void setBleConnStatus(BleConnStatus status) {
            LogUtil.v("=========== : 连接状态 ： " + status);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            printServices(gatt);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            LogUtil.v("Rssi : " + rssi);
        }
    }

    public static void printServices(BluetoothGatt gatt) {
        if (gatt != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                LogUtil.e("service: " + service.getUuid());
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    LogUtil.e("  characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                        LogUtil.e("        descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
                    }
                }
            }
        }
    }

}
