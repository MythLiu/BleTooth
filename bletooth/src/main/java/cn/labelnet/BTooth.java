package cn.labelnet;

import android.os.Build;

/**
 * @Package cn.labelnet
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:58 PM 2/5/2017
 * @Desc bletooth 包下为4.3 ~ 5.0
 * letooth 包下为 >= 5.0
 */

public class BTooth {

    /**
     * support Android L 5.0
     *
     * @return is support package android.bluetooth.le
     */
    private boolean isBuildLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
