package com.xinghai.test1;

public class Test1 {
    public static void print(int N) {
        int c = 0;
        int n = (int) Math.floor(-1.0 / 2 + Math.sqrt(N * 2 - 1) / 2) + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                System.out.print(' ');
            }
            for (int j = 0; j < 2 * i + 1; j++) {
                c++;
                System.out.print('*');
            }
            System.out.println();
        }
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j < n - 1 - i; j++) {
                System.out.print(' ');
            }
            for (int j = 0; j < 2 * i + 1; j++) {
                c++;
                System.out.print('*');
            }
            System.out.println();
        }
        int b = N-c;
        System.out.println("剩余*--->"+ b);
    }

    public static void main(String[] args) {
        print(18);/*
        int a = (int) (-1.0 / 2);
        System.out.println(a);
        int b = (int) (Math.sqrt(14 * 2 - 1) / 2);
        System.out.println(b);
        int i = (int) Math.floor(a + b) + 1;
        System.out.println(i);*/
    }
}
