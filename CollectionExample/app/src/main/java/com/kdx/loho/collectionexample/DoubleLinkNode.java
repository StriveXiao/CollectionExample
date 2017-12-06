package com.kdx.loho.collectionexample;

/**
 * Author:    Xiao_Tian
 * Version    V1.0
 * Date:      17/12/4 11:08
 * Description:
 * Why & What is modified:
 */

public class DoubleLinkNode {
    public int data;
    public LinkNode next;
    public LinkNode prev;

    public DoubleLinkNode(int data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
