package com.kdx.loho.collectionexample;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Author:    Xiao_Tian
 * Version    V1.0
 * Date:      17/11/2 16:51
 * Description:
 * Why & What is modified:
 */

public class IteratorExample {
    public static void main(String args[]) {
        LinkedList<Integer> ll = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            ll.addLast(i);
        }

        Iterator<Integer> ii = ll.iterator();
        while (ii.hasNext())
            System.out.print(ii.next());
    }
}
