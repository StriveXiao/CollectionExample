package com.kdx.loho.collectionexample;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * Author:    Xiao_Tian
 * Version    V1.0
 * Date:      17/12/12 10:00
 * Description:
 * Why & What is modified:
 */

public class TreeSetTest {
    public static void main(String args[]) {
        TreeSet ts = new TreeSet(new MyComparator());
        ts.add(new Person("A", 24, "男"));
        ts.add(new Person("B", 23, "女"));
        ts.add(new Person("C", 18, "男"));
        ts.add(new Person("D", 18, "女"));
        ts.add(new Person("D", 20, "女"));
        ts.add(new Person("D", 20, "女"));

        System.out.println(ts);
        System.out.println(ts.size());
    }

    static class MyComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Person p1 = (Person) o1;
            Person p2 = (Person) o2;

            if (p1.age < p2.age) {
                return 1;
            }
            if (p1.age > p2.age) {
                return -1;
            }
            return p1.name.compareTo(p2.name);
        }

    }
}
