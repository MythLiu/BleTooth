package cn.labelnet.bletooth.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @Package cn.labelnet.bletooth
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 7:03 PM 2/8/2017
 * @Desc Desc
 * <p>
 * 中心设备进行操作蓝牙
 * (1)
 */

public class BleToothService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
