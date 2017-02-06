package cn.labelnet.bletooth.ble.scan;

import android.bluetooth.BluetoothAdapter;
import android.os.CountDownTimer;

import cn.labelnet.bletooth.exception.ScanTimeOutException;


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
    private long timeOutMillis = 5000;
    private long timeInterval = 200;

    //timer
    private CountDownTimer countDownTimer;


    public BleToothBleScanCallback(long timeOutMill) {

        if (timeOutMill >= 3000) {
            this.timeOutMillis = timeOutMill;
        } else {
            try {
                throw new ScanTimeOutException("Timeout set at least 3S !");
            } catch (ScanTimeOutException e) {
                e.printStackTrace();
            }
        }

        countDownTimer = new CountDownTimer(timeOutMillis, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                float process = (float) ((timeOutMillis - millisUntilFinished) / (timeOutMillis * 1.0));
                if (process > 0.9) {
                    process = 1.0f;
                }
                bleToothScanProcess(process);
            }

            @Override
            public void onFinish() {
                onScanCompleteListener.onScanFinish();
            }
        };

    }


    //scan listener
    private OnScanCompleteListener onScanCompleteListener;

    public void setOnScanCompleteListener(OnScanCompleteListener onScanCompleteListener) {
        this.onScanCompleteListener = onScanCompleteListener;
    }

    protected abstract void onScanStart();
    protected abstract void onScanFinish();

    //start scan
    public void onStartTimmer() {
        countDownTimer.start();
        setBleToothScanStatus(BleScanStatus.scaning);
        onScanStart();
    }


    //stop scan
    public void onStopTimmer() {
        countDownTimer.cancel();
        onScanFinish();
    }

    //scan status
    public abstract void setBleToothScanStatus(BleScanStatus status);

    //scan process
    protected abstract void bleToothScanProcess(float process);

    public interface OnScanCompleteListener {
        void onScanFinish();
    }

}
