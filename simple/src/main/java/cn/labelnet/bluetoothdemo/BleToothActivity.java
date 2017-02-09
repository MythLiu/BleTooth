package cn.labelnet.bluetoothdemo;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.labelnet.bletooth.BleTooth;
import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.ble.conn.BleConnStatus;
import cn.labelnet.bletooth.ble.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.ble.scan.BleScanStatus;
import cn.labelnet.bletooth.core.BleScanCallBack;
import cn.labelnet.bletooth.core.SimpleScanAndConnCallBack;
import cn.labelnet.bletooth.util.LogUtil;

public class BleToothActivity extends AppCompatActivity {


    private TextView tv;

    private static BleTooth bleTooth;
    private BleScanCallBack scanCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_tooth);

        bleTooth = BleTooth.getInstance(getApplicationContext());
        scanCallBack = new ScanCallBack(5000);
        tv = (TextView) findViewById(R.id.tv_data);

        findViewById(R.id.btn_ble_scan_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // scan start
                bleTooth.startScan(scanCallBack);
            }
        });

        findViewById(R.id.btn_ble_scan_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // scan stop
                bleTooth.onScanFinish();
            }
        });

        findViewById(R.id.btn_ble_conn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // conn start
            }
        });

        findViewById(R.id.btn_ble_conn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // conn stop
            }
        });

        findViewById(R.id.btn_ble_scan_conn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // conn stop
                bleTooth.scanAndConnect("05:04:03:02:01:00", new ConnCallBack(), new SimpleScanAndConnCallBack.OnScanAndConnListener() {

                    @Override
                    public void onScanStatus(SimpleScanAndConnCallBack.ScanAndConnStatus status) {
                        LogUtil.v("scanAndConnect : "+status);
                    }

                    @Override
                    public void onScanProcess(float process) {
                        LogUtil.v("scanAndConnect : "+process);
                    }
                });
            }
        });

    }

    private static class ScanCallBack extends BleScanCallBack {

        public ScanCallBack(long timeoutmills) {
            super(timeoutmills);
        }

        @Override
        public void onScanDevicesData(List<BleDevice> bleDevices) {
            LogUtil.v("onScanDevicesData : " + bleDevices);
        }

        @Override
        public void onScanProcess(float process) {
            LogUtil.v("onScanProcess : " + process);
        }

        @Override
        public void onScanStatus(BleScanStatus status) {
            LogUtil.v("onScanStatus : " + status);
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
//            printServices(gatt);
            printServices(bleTooth.getBluetoothGatt());
            LogUtil.e("---------------------------------------------------------");
            printServices(gatt);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            LogUtil.v("onCharacteristicChanged 收到的数据 ： " + characteristic.getValue());
        }

        public static void printServices(BluetoothGatt gatt) {
            if (gatt != null) {
                for (BluetoothGattService service : gatt.getServices()) {
                    LogUtil.e("service: " + service.getUuid());
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        LogUtil.e("  characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));
                        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                            LogUtil.e("     descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
                        }
                    }
                }
            }
        }

    }

}
