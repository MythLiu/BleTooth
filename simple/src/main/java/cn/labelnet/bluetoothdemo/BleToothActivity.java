package cn.labelnet.bluetoothdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.labelnet.bletooth.BleTooth;
import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.ble.scan.BleScanStatus;
import cn.labelnet.bletooth.core.BleScanCallBack;
import cn.labelnet.bluetoothdemo.callback.ConnCallBack;

public class BleToothActivity extends AppCompatActivity {


    private TextView tv;

    private BleTooth bleTooth;
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
                bleTooth.scanAndConnect("77:97:A5:75:40:98",false,new ConnCallBack());
            }
        });

    }

    private static class ScanCallBack extends BleScanCallBack {

        public ScanCallBack(long timeoutmills) {
            super(timeoutmills);
        }

        @Override
        public void onScanDevicesData(List<BleDevice> bleDevices) {

        }

        @Override
        public void onScanProcess(float process) {

        }

        @Override
        public void onScanStatus(BleScanStatus status) {

        }
    }


}
