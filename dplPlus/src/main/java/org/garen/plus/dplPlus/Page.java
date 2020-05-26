package org.garen.plus.dplPlus;

import java.util.List;

/**
 * <p>
 * 功能描述 : 描述
 * </p>
 *
 * @author : Garen Gosling 2020/5/25 上午11:45
 */
public class Page<T> {

    /**
     * 当前索引
     */
    private int current;

    /**
     * 每页数量
     */
    private int size;

    /**
     * 页码
     */
    private int pageNo = 0;

    /**
     * 总数
     */
    private int totalCount = 0;

    /**
     * 总页数
     */
    private int pageAll = 0;

    /**
     * 数据集合数量
     */
    private int count = 0;

    /**
     * 数据集合
     */
    private List<T> pageList = null;

    /**
     * <p>
     * 功能描述 : 构造
     * </p>
     *
     * @author : Garen Gosling   2020/5/25 下午3:03
     *
     * @param current 当前索引
     * @param size 每页数量
     * @param list 源数据集合
     * @Return
     **/
    public Page(int current, int size, List<T> list) {
        // size 默认为 10
        if(size < 1) {
            this.size = 10;
        }else{
            this.size = size;
        }

        // current标准值，与size有关，比如size=10，current=13，则改current=10
        if(current < this.size) {
            this.current = 0;
        } else {
            this.current = current / size * size;
        }

        // 分页
        if(!MyCollectionUtils.isEmpty(list)){
            // 页码
            this.pageNo = this.current / size + 1;

            // 总数
            this.totalCount = list.size();

            // 总页数
            this.pageAll = this.totalCount % this.size == 0 ? this.totalCount / this.size : this.totalCount / this.size + 1;

            // 数据集合
            int toIndex = this.current + this.size < list.size() ? this.current + this.size : list.size();
            this.pageList = list.subList(this.current, toIndex);

            // 数据集合数量
            this.count = this.pageList.size();
        }

    }

    public int getCurrent() {
        return current;
    }

    public int getSize() {
        return size;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageAll() {
        return pageAll;
    }

    public int getCount() {
        return count;
    }

    public List<T> getPageList() {
        return pageList;
    }

}
