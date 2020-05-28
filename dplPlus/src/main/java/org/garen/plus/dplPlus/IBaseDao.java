package org.garen.plus.dplPlus;

import java.util.List;

/**
 * <p>
 * 功能描述 : 数据库映射父接口
 * </p>
 *
 * @author : Garen Gosling 2020/5/25 上午9:55
 */
public interface IBaseDao<PK, E> {
    /**
     * <p>
     * 功能描述 : 新增
     * </p>
     *
     * @author : Garen Gosling   2020/5/25 下午4:55
     *
     * @param pk 主键
     * @param e 实体
     * @Return E 返回实体对象
     **/
    E save(PK pk, E e);

    /**
     * <p>
     * 功能描述 : 修改
     * </p>
     *
     * @author : Garen Gosling   2020/5/25 下午4:55
     *
     * @param pk 主键
     * @param e 实体
     * @Return E 返回实体对象
     **/
    E update(PK pk, E e);

    /**
     * <p>
     * 功能描述 : 主键查询
     * </p>
     *
     * @author : Garen Gosling   2020/5/25 下午4:56
     *
     * @param pk 主键
     * @Return E 返回实体对象
     **/
    E get(PK pk);

    /**
     * <p>
     * 功能描述 : 删除
     * </p>
     *
     * @author : Garen Gosling   2020/5/25 下午4:56
     *
     * @param pk 主键
     * @Return void
     **/
    void delete(PK pk);

    /**
     * <p>
     * 功能描述 : 查询全部
     * </p>
     *
     * @author : Garen Gosling   2020/5/25 下午5:48
     *
     * @param
     * @Return java.util.List<E>
     **/
    List<E> listAll();

    /**
     * <p>
     * 功能描述 : 二级索引查询
     * </p>
     *
     * @author : Garen Gosling   2020/5/25 下午5:48
     *
     * @param keyName 索引名称
     * @param keyClass 索引类型
     * @param sk 索引值
     * @Return java.util.List<E>
     **/
    <SK> List<E> listBySk(String keyName, Class<SK> keyClass, SK sk);

    /**
     * <p>
     * 功能描述 : 参数查询
     * </p>
     *
     * @author : Garen Gosling   2020/5/28 下午2:24
     *
     * @param iParamsHandler 提供参数列表的接口
     * @Return java.util.List<E>
     **/
    List<E> listByParams(IParamsHandler iParamsHandler);

    /**
     * <p>
     * 功能描述 : 分页查询
     * </p>
     *
     * @author : Garen Gosling   2020/5/25 下午5:49
     *
     * @param current 起始索引
     * @param size 每页数量
     * @param list 源数据集合
     * @Return ogd.berkeleyDB.easyDPL.dplPlus.Page<T>
     **/
    <T> Page<T> pageAll(Integer current, Integer size, List<T> list);

    /**
     * <p>
     * 功能描述 : 通用方法，有事务
     * </p>
     *
     * @author : Garen Gosling   2020/5/23 下午3:02
     *
     * @param iCurdHandler 自定义数据库操作接口（lambda表达式）
     * @Return T 返回类型
     **/
    <T> T execute(ICurdHandler<T> iCurdHandler);

    /**
     * <p>
     * 功能描述 : 组合成参数集合
     * </p>
     *
     * @author : Garen Gosling   2020/5/26 上午10:06
     *
     * @param params 参数
     * @Return java.util.List<ogd.berkeleyDB.easyDPL.dplPlus.Param>
     **/
    List<Param> toParamList(Param... params);
}
