package com.kdx.loho.collectionexample;

/**
 * Author:    Xiao_Tian
 * Version    V1.0
 * Date:      17/12/6 16:24
 * Description:
 * Why & What is modified:
 */

public class LinkedListExample {
    public static void main(String args[]) {
        LinkNode head = new LinkNode(1);
        head.next = new LinkNode(2);


        LinkNode newNode = new LinkNode(3);
        newNode.next = head.next;
        head.next = newNode;

        head.next = head.next.next;
    }

    public static int queryData(LinkNode head, int index) {
        LinkNode cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }

        return cur.data;
    }

    public static LinkNode findPrevious(LinkNode head, LinkNode temp) {
        LinkNode cur = head;
        while (cur != null) {
            if (cur.next == temp)
                return cur;

            cur = cur.next;
        }

        return null;
    }
}
