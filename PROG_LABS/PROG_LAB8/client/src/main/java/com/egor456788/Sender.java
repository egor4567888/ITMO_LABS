package com.egor456788;

import com.egor456788.entities.Entity;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static com.egor456788.Applicaton.*;

public class Sender {
    static DatagramChannel clientChannel;

    static {
        try {
            clientChannel = DatagramChannel.open();
            clientChannel.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static DatagramSocket ds;

    static {
        try {
            ds = new DatagramSocket(clientPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
    public static String sendToServer(String command, String comArgs, Entity entity,String userName) {

        String password = "";
        ByteBuffer buffer = ByteBuffer.allocate(bufSize);
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", Applicaton.serverPort);
        byte[] DtgrByteArr = new byte[bufSize];
        String receivedMessage;



        boolean commandSent = false;

        for (int attempt = 1; attempt <= maxAttempts && !commandSent; attempt++) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(new Request(command, comArgs, entity, clientPort, userName, password));
                buffer.clear();
                buffer.put(baos.toByteArray());
                buffer.flip();

                clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу

                DatagramPacket dp = new DatagramPacket(DtgrByteArr, DtgrByteArr.length);
                ds.setSoTimeout(5000);
                ds.receive(dp);
                receivedMessage = new String(dp.getData(), 0, dp.getLength());
                return (receivedMessage);

            } catch (SocketTimeoutException e) {
                System.out.println("Не удалось подключиться к серверу для отправки команды, попытка " + attempt + " из " + maxAttempts);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Не удалось подключиться к серверу после " + maxAttempts + " попыток.");
                }
            } catch (IOException e) {
                System.out.println("Ошибка подключения к серверу: " + e);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static String sendToServer(String command, String comArgs, Entity entity) {


        ByteBuffer buffer = ByteBuffer.allocate(bufSize);
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", Applicaton.serverPort);
        byte[] DtgrByteArr = new byte[bufSize];
        String receivedMessage;
        String userName = "";
        String password = "";


        boolean commandSent = false;

        for (int attempt = 1; attempt <= maxAttempts && !commandSent; attempt++) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(new Request(command, comArgs, entity, clientPort, userName, password));
                buffer.clear();
                buffer.put(baos.toByteArray());
                buffer.flip();

                clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу

                DatagramPacket dp = new DatagramPacket(DtgrByteArr, DtgrByteArr.length);
                ds.setSoTimeout(5000);
                ds.receive(dp);
                receivedMessage = new String(dp.getData(), 0, dp.getLength());
                return (receivedMessage);

            } catch (SocketTimeoutException e) {
                System.out.println("Не удалось подключиться к серверу для отправки команды, попытка " + attempt + " из " + maxAttempts);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Не удалось подключиться к серверу после " + maxAttempts + " попыток.");
                }
            } catch (IOException e) {
                System.out.println("Ошибка подключения к серверу: " + e);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
}
    public static String sendToServer(String command) {
        Entity entity = null;
        String comArgs = null;

        ByteBuffer buffer = ByteBuffer.allocate(bufSize);
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", Applicaton.serverPort);
        byte[] DtgrByteArr = new byte[bufSize];
        String receivedMessage;
        String userName = "";
        String password = "";


        boolean commandSent = false;

        for (int attempt = 1; attempt <= maxAttempts && !commandSent; attempt++) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(new Request(command, comArgs, entity, clientPort, userName, password));
                buffer.clear();
                buffer.put(baos.toByteArray());
                buffer.flip();

                clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу

                DatagramPacket dp = new DatagramPacket(DtgrByteArr, DtgrByteArr.length);
                ds.setSoTimeout(5000);
                ds.receive(dp);
                receivedMessage = new String(dp.getData(), 0, dp.getLength());
                return (receivedMessage);

            } catch (SocketTimeoutException e) {
                System.out.println("Не удалось подключиться к серверу для отправки команды, попытка " + attempt + " из " + maxAttempts);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Не удалось подключиться к серверу после " + maxAttempts + " попыток.");
                }
            } catch (IOException e) {
                System.out.println("Ошибка подключения к серверу: " + e);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }
    public static String sendToServer(Request request) {

        ByteBuffer buffer = ByteBuffer.allocate(bufSize);
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", Applicaton.serverPort);
        byte[] DtgrByteArr = new byte[bufSize];
        String receivedMessage;

        boolean commandSent = false;

        for (int attempt = 1; attempt <= maxAttempts && !commandSent; attempt++) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(request);
                buffer.clear();
                buffer.put(baos.toByteArray());
                buffer.flip();

                clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу

                DatagramPacket dp = new DatagramPacket(DtgrByteArr, DtgrByteArr.length);
                ds.setSoTimeout(5000);
                ds.receive(dp);
                receivedMessage = new String(dp.getData(), 0, dp.getLength());
                return (receivedMessage);

            } catch (SocketTimeoutException e) {
                System.out.println("Не удалось подключиться к серверу для отправки команды, попытка " + attempt + " из " + maxAttempts);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Не удалось подключиться к серверу после " + maxAttempts + " попыток.");
                }
            } catch (IOException e) {
                System.out.println("Ошибка подключения к серверу: " + e);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }
    public static CommandsPack getCommandPack() {
        CommandsPack commandsPack;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(bufSize);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(new Request("get_commands", clientPort, null, null));
                buffer.put(baos.toByteArray());
                buffer.flip();

                clientChannel.send(buffer, serverAddress);
                oos.close();

                byte[] arr = new byte[bufSize];
                DatagramPacket dp = new DatagramPacket(arr, arr.length);
                ds.setSoTimeout(10000);
                ds.receive(dp);
                ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                ois.close();

                commandsPack =  (CommandsPack) ois.readObject();
                if (!(commandsPack == null)){
                    return commandsPack;
                }

            } catch (SocketTimeoutException e) {
                System.out.println("Не удалось подключиться к серверу, попытка " + attempt + " из " + maxAttempts);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(attemptDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Не удалось подключиться к серверу после " + maxAttempts + " попыток.");
                    System.exit(0);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Не удалось подключиться к серверу: " + e);
                System.exit(0);
            }
        }
        return null;
    }
}
