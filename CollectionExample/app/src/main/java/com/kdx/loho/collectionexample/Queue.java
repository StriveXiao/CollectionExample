package com.kdx.loho.collectionexample;

/**
 * Author:    Xiao_Tian
 * Version    V1.0
 * Date:      17/12/18 15:38
 * Description:
 * Why & What is modified:
 */

public interface Queue {
    public boolean add(Object elem); // 将一个元素放到队尾，如果成功，返回true
    public Object remove(); // 将一个元素从队头删除，如果成功，返回true
}
