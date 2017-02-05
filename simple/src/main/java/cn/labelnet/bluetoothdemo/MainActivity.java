package cn.labelnet.bluetoothdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import cn.labelnet.bletooth.BleTooth;
import cn.labelnet.bletooth.bean.BleDevice;
import cn.labelnet.bletooth.scan.ScanResultCallback;
import cn.labelnet.bletooth.scan.ScanStatus;
import cn.labelnet.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    private BleTooth bleTooth;
    private ScanCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleTooth = new BleTooth(getApplicationContext());
        callBack = new ScanCallBack(10000);

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
    }

    private static class ScanCallBack extends ScanResultCallback {

        public ScanCallBack(long timeOutMillis) {
            super(timeOutMillis);
        }

        @Override
        protected void onNotifyBleToothDeviceRssi(int position, int rssi) {

        }

        @Override
        protected void onScanComplete(List<BleDevice> bleDevices) {

        }

        @Override
        public void setBleToothScanStatus(ScanStatus status) {
            LogUtil.v("当前状态 ： " + status);
        }

        @Override
        protected void bleToothScanProcess(float process) {
            LogUtil.v("Process : " + process);
        }
    }

}
