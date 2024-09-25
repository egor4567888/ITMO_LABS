package com.egor456788;

import com.egor456788.entities.Entity;
import com.egor456788.exceptions.InputException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Scanner;

public class Applicaton {

    private static final int bufSize = 1024 * 64;
    private static final int maxAttempts = 5;
    private static final int attemptDelay = 5000; // 5 секунд

    public void run(String[] args) {
        Printer printer = new Printer();
        BufferedReader readerSystemIn = new BufferedReader(new InputStreamReader(System.in));
        CommandsPack commands = null;
        int clientPort = 6788;
        int serverPort = 9875;
        boolean isConnected = false;

        for (int attempt = 1; attempt <= maxAttempts && !isConnected; attempt++) {
            try (DatagramChannel clientChannel = DatagramChannel.open(); DatagramSocket ds = new DatagramSocket(clientPort)) {

                clientChannel.configureBlocking(false); // Не блокировать поток при чтении и записи
                ByteBuffer buffer = ByteBuffer.allocate(bufSize);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(new Request("get_commands", clientPort, null, null));
                buffer.put(baos.toByteArray());
                buffer.flip();
                InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort); // Адрес сервера
                clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу
                oos.close();

                byte[] arr = new byte[bufSize];
                DatagramPacket dp = new DatagramPacket(arr, arr.length);
                ds.setSoTimeout(10000);
                ds.receive(dp);
                ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                commands = (CommandsPack) ois.readObject();

                ois.close();
                isConnected = true;

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

        if (!isConnected) {
            System.out.println("Не удалось подключиться к серверу.");
            System.exit(0);
        }

        String line = null;

        try (Scanner scanner = new Scanner(System.in); DatagramChannel clientChannel = DatagramChannel.open(); DatagramSocket ds = new DatagramSocket(clientPort)) {

            clientChannel.configureBlocking(false);
            String comArgs;
            Entity entity;
            ByteBuffer buffer = ByteBuffer.allocate(bufSize);
            InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort);
            byte[] DtgrByteArr = new byte[bufSize];
            String receivedMessage;
            String userName = "";
            String password = "";
            boolean isLoggedIn = false;
            String command;
            while (!isLoggedIn) {
                System.out.println("1 Войти, 2 Зарегистрироваться");
                comArgs = null;
                entity = null;
                line = scanner.nextLine().trim();
                if (!(line.equals("1") || line.equals("2"))) {
                    printer.println("Введите 1 для авторизации или 2 для регистрации");
                } else {
                    if (line.equals("1")) {
                        command = "login_alter";
                    } else {
                        command = "register_alter";
                    }
                    printer.println("Введите имя");
                    userName = scanner.nextLine();
                    printer.println("Введите пароль");
                    password = hashPassword(scanner.nextLine());

                    boolean loginSuccessful = false;

                    for (int attempt = 1; attempt <= maxAttempts && !loginSuccessful; attempt++) {
                        try {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(new Request(command, clientPort, userName, password, -1));
                            buffer.clear();
                            buffer.put(baos.toByteArray());
                            buffer.flip();

                            clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу

                            DatagramPacket dp = new DatagramPacket(DtgrByteArr, DtgrByteArr.length);
                            ds.setSoTimeout(5000);
                            ds.receive(dp);
                            receivedMessage = new String(dp.getData(), 0, dp.getLength());
                            printer.println(receivedMessage);
                            if (receivedMessage.contains("успешно")) {
                                isLoggedIn = true;
                                loginSuccessful = true;
                            }

                        } catch (SocketTimeoutException e) {
                            System.out.println("Не удалось подключиться к серверу для авторизации, попытка " + attempt + " из " + maxAttempts);
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
                        } catch (IOException e) {
                            System.out.println("Ошибка подключения к серверу: " + e);
                            if (attempt < maxAttempts) {
                                try {
                                    Thread.sleep(attemptDelay);
                                } catch (InterruptedException ie) {
                                    Thread.currentThread().interrupt();
                                }
                            } else {
                                System.exit(0);
                            }
                        }
                    }

                    if (!loginSuccessful) {
                        System.out.println("Не удалось подключиться к серверу для авторизации.");
                        System.exit(0);
                    }
                }
            }

            printer.println("Введите help для вывода информации о командах");
            while (scanner.hasNextLine()) {
                comArgs = null;
                entity = null;
                line = scanner.nextLine().trim();
                String[] input = line.split(" ");
                if (input.length > 2) {
                    printer.println(line + ": ОШИБКА избыточное число аргументов");
                } else {
                    if (input.length == 2) {
                        comArgs = input[1];
                    }

                    clientChannel.configureBlocking(false); // Не блокировать поток при чтении и записи
                    if ((commands.getCommands()).contains(input[0])) {
                        try {
                            entity = Creator.create(printer, readerSystemIn, userName, false);
                        } catch (InputException e) {
                            printer.println(line + ": " + e.getMessage() + " ВВЕДИТЕ КОМАНДУ ЗАНОВО");
                            continue;
                        }
                    }

                    boolean commandSent = false;

                    for (int attempt = 1; attempt <= maxAttempts && !commandSent; attempt++) {
                        try {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(new Request(input[0], comArgs, entity, clientPort, userName, password));
                            buffer.clear();
                            buffer.put(baos.toByteArray());
                            buffer.flip();

                            clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу

                            DatagramPacket dp = new DatagramPacket(DtgrByteArr, DtgrByteArr.length);
                            ds.setSoTimeout(5000);
                            ds.receive(dp);
                            receivedMessage = new String(dp.getData(), 0, dp.getLength());
                            printer.println(receivedMessage);
                            commandSent = true;

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
                                System.exit(0);
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

