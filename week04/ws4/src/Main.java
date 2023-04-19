//package com.company;
public class Main {
    static <T> boolean isEqual (T l, T r) {
        return l.equals(r);
    }
    public static void main(String[] args) {

        Integer i = 10;
        String s = "sion";

        System.out.println(isEqual(i,s));
        System.out.println(Main.<Integer>isEqual(i,s));
    }
}

//TASK1
//static bool isEqual(Object<T> left, Object<T> right) {
//
//    return left.equals(right);
//}