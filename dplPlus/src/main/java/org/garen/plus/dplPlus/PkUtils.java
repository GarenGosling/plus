package org.garen.plus.dplPlus;

import java.util.UUID;

/**
 * <p>
 * 功能描述 : 主键工具类
 * </p>
 *
 * @author : Garen Gosling 2020/5/25 上午11:06
 */
public class PkUtils {
    /**
     * <p>
     * 功能描述 : 32位 UUID
     * </p>
     *
     * @author : Garen Gosling   2020/5/23 下午3:14
     *
     * @param
     * @Return java.lang.String
     **/
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
