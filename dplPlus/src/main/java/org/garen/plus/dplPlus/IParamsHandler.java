package org.garen.plus.dplPlus;

import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

import java.util.List;

/**
 * <p>
 * 功能描述 : 参数集合
 * </p>
 *
 * @author : Garen Gosling 2020/5/23 上午11:20
 */
public interface IParamsHandler<SK, PK, E> {
    List<Param<SK, PK, E>> paramList(EntityStore store, PrimaryIndex<PK, E> pi);
}
