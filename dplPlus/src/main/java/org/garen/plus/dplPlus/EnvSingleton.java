package org.garen.plus.dplPlus;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import java.io.File;

/**
 * <p>
 * 功能描述 : 环境，单例
 * </p>
 *
 * @author : Garen Gosling 2020/5/22 下午3:46
 */
public class EnvSingleton {

    // 环境
    private Environment env;

    // 私有构造函数
    private EnvSingleton() {
        String envPath = DplConfig.getInstance().getEnvPath();
        if(!MyStringUtils.isEmpty(envPath)){
            // 环境目录
            File envHome = new File(envPath);
            if (!envHome.exists()) envHome.mkdirs();
            // 环境配置
            EnvironmentConfig myEnvConfig = new EnvironmentConfig();
            myEnvConfig.setAllowCreate(true);  // 环境文件不存在是否要创建，true创建、false抛异常
            myEnvConfig.setTransactional(true); // 开启事务
            // 环境对象
            env = new Environment(envHome, myEnvConfig);
        }
    }

    // 单例对象 volatile + 双重检测机制 -> 禁止指令重排
    private volatile static EnvSingleton instance = null;

    // 静态工厂方法
    public static EnvSingleton getInstance() {
        if(instance == null) {  // 双重检测机制   // B
            synchronized (EnvSingleton.class) {   // 同步锁
                if(instance == null){
                    instance = new EnvSingleton();    // A - 3
                }
            }
        }
        return instance;
    }

    public Environment getEnv() {
        return env;
    }

}
