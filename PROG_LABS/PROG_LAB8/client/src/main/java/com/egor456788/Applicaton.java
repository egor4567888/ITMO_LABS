package com.egor456788;

import com.egor456788.entities.Entity;
import com.egor456788.exceptions.InputException;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Scanner;

public class Applicaton {

    static final int bufSize = 1024 * 64;
    static final int maxAttempts = 5;
    static final int attemptDelay = 5000; // 5 секунд
    static int clientPort = 6788;
    static int serverPort = 9875;
    static InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort);
    static Locale locale = Locale.forLanguageTag("en");

    public void run(String[] args) {
        System.out.println(Sender.sendToServer("show"));
        Printer printer = new Printer();
        BufferedReader readerSystemIn = new BufferedReader(new InputStreamReader(System.in));
        CommandsPack commands = null;

        boolean isConnected = false;




        commands = Sender.getCommandPack();

        if (commands == null) {
            System.out.println("Не удалось подключиться к серверу.");
            System.exit(0);
        }




            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginGUI();
                }
            });

    }


    public static String hashPassword(String password)  {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

