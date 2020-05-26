package org.garen.plus.dplPlus;

import com.sleepycat.persist.SecondaryIndex;

/**
 * <p>
 * 功能描述 : 参数
 * </p>
 *
 * @author : Garen Gosling 2020/5/26 上午9:21
 */
public class Param<SK, PK, E> {
    /**
     * 二级索引
     */
    private SecondaryIndex<SK, PK, E> secondaryIndex;

    /**
     * 参数值
     */
    private SK key;

    public Param(SecondaryIndex<SK, PK, E> secondaryIndex, SK key) {
        this.secondaryIndex = secondaryIndex;
        this.key = key;
    }

    public Param(SecondaryIndex<SK, PK, E> secondaryIndex) {
        this.secondaryIndex = secondaryIndex;
    }

    public Param(SK key) {
        this.key = key;
    }

    public SecondaryIndex<SK, PK, E> getSecondaryIndex() {
        return secondaryIndex;
    }

    public void setSecondaryIndex(SecondaryIndex<SK, PK, E> secondaryIndex) {
        this.secondaryIndex = secondaryIndex;
    }

    public SK getKey() {
        return key;
    }

    public void setKey(SK key) {
        this.key = key;
    }
}
