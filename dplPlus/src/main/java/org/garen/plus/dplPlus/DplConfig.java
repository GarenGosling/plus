package org.garen.plus.dplPlus;

/**
 * <p>
 * 功能描述 : 配置环境、配置仓库名称
 * </p>
 *
 * @author : Garen Gosling 2020/5/26 下午2:18
 */
public class DplConfig {
    // 环境
    private String envPath;
    // 仓库名称
    private String storeName;

    // 私有构造函数
    private DplConfig() {}

    // 单例对象 volatile + 双重检测机制 -> 禁止指令重排
    private volatile static DplConfig instance = null;

    // 静态工厂方法
    public static DplConfig getInstance() {
        if(instance == null) {  // 双重检测机制   // B
            synchronized (DplConfig.class) {   // 同步锁
                if(instance == null){
                    instance = new DplConfig();    // A - 3
                }
            }
        }
        return instance;
    }

    /**
     * <p>
     * 功能描述 : 初始化，使用DPL-PLUS首先要初始化！！！
     * </p>
     *
     * @author : Garen Gosling   2020/5/26 下午2:22
     *
     * @param envPath 环境路径
     * @param storeName 仓库名称
     * @Return ogd.berkeleyDB.easyDPL.dplPlus.DplConfig
     **/
    public DplConfig init(String envPath, String storeName) {
        if(!MyStringUtils.isEmpty(envPath)) this.envPath = envPath;
        if(!MyStringUtils.isEmpty(storeName)) this.storeName = storeName;
        EnvSingleton.getInstance();
        return this;
    }

    /**
     * <p>
     * 功能描述 : 获取环境路径
     * </p>
     *
     * @author : Garen Gosling   2020/5/26 下午2:24
     *
     * @param
     * @Return java.lang.String
     **/
    public String getEnvPath() {
        return envPath;
    }

    /**
     * <p>
     * 功能描述 : 获取仓库名
     * </p>
     *
     * @author : Garen Gosling   2020/5/26 下午2:24
     *
     * @param
     * @Return java.lang.String
     **/
    public String getStoreName() {
        return storeName;
    }

}
