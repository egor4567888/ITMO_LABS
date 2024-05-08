package com.egor456788;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Класс для блокировки и разблокировки потока вывода
 */
public class OutputBlocker {
    private static PrintStream originalOut = System.out;
    private static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private static PrintStream fakeOut = new PrintStream(outputStream);

    public static void blockOutput() {
        System.setOut(fakeOut);
    }

    public static void unblockOutput() {
        System.setOut(originalOut);
    }

}
