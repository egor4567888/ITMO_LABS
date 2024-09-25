package com.egor456788;

/**
 * Класс для вывода
 */
public class Printer {
    public void print(Object o){
        System.out.print(o);
    }
    public void println(Object o){
        System.out.println(o);
    }
    public void printErr(Object o){
        System.out.println("ошибка: " + o);
    }
}
