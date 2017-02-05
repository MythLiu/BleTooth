package cn.labelnet.letooth.scan;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanCallback;
import android.os.Build;

/**
 * @Package cn.labelnet.letooth.scan
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:00 PM 2/5/2017
 * @Desc Desc
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public abstract class BleToothLeScanCallBack extends ScanCallback{
}
