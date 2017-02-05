package cn.labelnet.bletooth.scan;

import android.bluetooth.BluetoothAdapter;
import android.os.CountDownTimer;

import java.util.List;

import cn.labelnet.bletooth.bean.BleDevice;
import cn.labelnet.util.LogUtil;


/**
 * @Package cn.labelnet.bletooth.scan
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:37 PM 2/5/2017
 * @Desc scan
 * (1) timeout
 * (2) scan status
 */

public abstract class BleToothScanCallback implements BluetoothAdapter.LeScanCallback {


    private static final String TAG = BleToothScanCallback.class.getSimpleName();
    //timeout millis
    private long timeOutMillis = 3000;
    private long timeInterval = 100;

    public void setTimeOutMillis(long timeOutMillis) {
        this.timeOutMillis = timeOutMillis;
    }

    //timer
    private CountDownTimer countDownTimer = new CountDownTimer(timeOutMillis, timeInterval) {

        @Override
        public void onTick(long millisUntilFinished) {
            float process = (float) ((timeOutMillis - millisUntilFinished) / (timeOutMillis * 1.0));
            LogUtil.v(TAG, "scan millis : " + millisUntilFinished);
            LogUtil.v(TAG, "scan process : " + process);
            bleToothScanProcess(process);
        }

        @Override
        public void onFinish() {
            onScanComplete(getBleDevices());
        }
    };

    // scan device info
    protected abstract List<BleDevice> getBleDevices();

    // scan complete
    protected abstract void onScanComplete(List<BleDevice> bleDevices);

    //start scan
    public void onStartTimmer() {
        countDownTimer.start();
        setBleToothScanStatus(ScanStatus.scaning);
    }
    //stop scan
    public void onStopTimmer() {
        countDownTimer.cancel();
        setBleToothScanStatus(ScanStatus.disscan);
    }

    //scan status
    public abstract void setBleToothScanStatus(ScanStatus status);

    //scan process
    protected abstract void bleToothScanProcess(float process);

}
