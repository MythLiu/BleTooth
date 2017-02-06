package cn.labelnet.bletooth.ble.scan;

import android.bluetooth.BluetoothAdapter;
import android.os.CountDownTimer;
import java.util.List;
import cn.labelnet.bletooth.ble.bean.BleDevice;
import cn.labelnet.bletooth.util.LogUtil;


/**
 * @Package cn.labelnet.bletooth.ble.scan
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:37 PM 2/5/2017
 * @Desc scan
 * (1) timeout
 * (2) scan status
 */

public abstract class BleToothBleScanCallback implements BluetoothAdapter.LeScanCallback {


    private static final String TAG = BleToothBleScanCallback.class.getSimpleName();
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
        setBleToothScanStatus(BleScanStatus.scaning);
    }
    //stop scan
    public void onStopTimmer() {
        countDownTimer.cancel();
        setBleToothScanStatus(BleScanStatus.disscan);
    }

    //scan status
    public abstract void setBleToothScanStatus(BleScanStatus status);

    //scan process
    protected abstract void bleToothScanProcess(float process);

}
