package com.kdx.loho.collectionexample;

import android.support.annotation.NonNull;

/**
 * Author:    Xiao_Tian
 * Version    V1.0
 * Date:      17/12/12 09:56
 * Description:
 * Why & What is modified:
 */

public class Person implements Comparable {
    public String name;
    public int age;
    public String gender;

    public Person() {

    }

    public Person(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", gender=" + gender
                + "]\r\n";
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Person p = (Person) o;
        if (this.age > p.age) {
            return 1;
        }
        if (this.age < p.age) {
            return -1;
        }
        return this.name.compareTo(p.name);
    }
}
