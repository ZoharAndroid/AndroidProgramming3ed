package com.zohar.criminalintent;

/**
 * interface ： recyclerview的策侧滑删除接口
 *
 */
public interface IOperationData {

    /**
     * 移动item
     *
     * @param fromPosition 当前item的位置
     * @param toPosition 需要移动的位置
     */
    void onItemMove(int fromPosition, int toPosition);

    /**
     * 移除item
     *
     * @param position 需要移除item的位置
     */
    void onItemRemove(int position);

}
