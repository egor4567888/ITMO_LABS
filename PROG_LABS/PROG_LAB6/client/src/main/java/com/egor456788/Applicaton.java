package com.egor456788;

import com.egor456788.commands.*;
import com.egor456788.entities.Entity;
import com.egor456788.exceptions.InputException;
import com.egor456788.menegers.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.Scanner;

public class Applicaton {
    public static CommandManager commandManager = null;

    public void run(String[] args) {
        Printer printer = new Printer();
        BufferedReader readerSystemIn = new BufferedReader(new InputStreamReader(System.in));
        CommandsPack commands = null;
        int clientPort =  6789;
        int serverPort = 9876;

        try (DatagramChannel clientChannel = DatagramChannel.open(); DatagramSocket ds = new DatagramSocket(clientPort)) {

            clientChannel.configureBlocking(false); // Не блокировать поток при чтении и записи
            ByteBuffer buffer = ByteBuffer.allocate(100024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(new Request("get_commands",clientPort));
            buffer.put(baos.toByteArray());
            buffer.flip();
            InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort); // Адрес сервера
            clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу
            oos.close();

            byte[] arr = new byte[10024];
            DatagramPacket dp = new DatagramPacket(arr,arr.length);
            ds.receive(dp);
            ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            commands = (CommandsPack) ois.readObject();
            ois.close();


        } catch (IOException e) {
            System.out.println("Не удалось подключится к серверу: " + e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String line = null;
        printer.println("Введите help для вывода информации о командах");
        try (Scanner scanner = new Scanner(System.in); DatagramChannel clientChannel = DatagramChannel.open();DatagramSocket ds = new DatagramSocket(clientPort);) {
            String comArgs;
            Entity entity;
            ByteBuffer buffer = ByteBuffer.allocate(100024);
            InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort);
            byte[] DtgrByteArr = new byte[100024];
            String receivedMessage;
            while (scanner.hasNextLine()) {
                comArgs = null;
                entity = null;
                line = scanner.nextLine().trim();
                String[] input = line.split(" ");
                if (input.length>2)
                    printer.println(line + ": ОШИБКА избыточное число аргументов");

                else {
                    if (input.length ==2 )
                        comArgs = input[1];

                    clientChannel.configureBlocking(false); // Не блокировать поток при чтении и записи
                    if ((commands.getCommands()).contains(input[0]))
                        try {
                            entity = Creator.create(printer, readerSystemIn, false);
                        }catch (InputException e){
                            printer.println(line + ": " + e.getMessage() + " ВВЕДИТЕ КОМАНДУ ЗАНОВО");
                            continue;
                        }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(new Request(input[0], comArgs, entity, clientPort));
                    buffer.clear();
                    buffer.put(baos.toByteArray());
                    buffer.flip();

                    clientChannel.send(buffer, serverAddress); // Отправляем сообщение серверу


                    DatagramPacket dp = new DatagramPacket(DtgrByteArr,DtgrByteArr.length);
                    ds.receive(dp);
                    receivedMessage = new String(dp.getData(), 0, dp.getLength());
                    printer.println(receivedMessage);

                }
            }
            System.exit(0);

        }  catch (SocketException e) {
            printer.println("Ошибка подключения к серверу" + e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
