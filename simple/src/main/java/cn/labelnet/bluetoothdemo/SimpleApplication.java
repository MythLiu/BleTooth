package cn.labelnet.bluetoothdemo;

import cn.labelnet.bletooth.core.BaseBleToothService;
import cn.labelnet.bletooth.core.simple.SimpleBleApplication;

/**
 * @Package cn.labelnet.bluetoothdemo
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 9:06 PM 2/10/2017
 * @Desc Desc
 */

public class SimpleApplication extends SimpleBleApplication {

    public static BaseBleToothService getBleService() {
        return mBleToothService;
    }

}
