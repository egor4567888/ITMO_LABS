package com.egor456788;

import com.egor456788.commands.*;
import com.egor456788.menegers.CollectionMeneger;
import com.egor456788.menegers.CommandManager;
import com.egor456788.menegers.Invoker;
import com.egor456788.menegers.Printer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Applicaton {

    public static CommandsPack commandsPack = new CommandsPack(new  ArrayList<String>(Arrays.asList(new String[]{"update_id","add","add_if_min"})));
    public void run(String[] args) {
        Printer printer = new Printer();
        CommandManager commandManager = new CommandManager();
        CollectionMeneger collectionMeneger = new CollectionMeneger();
        collectionMeneger.addFromFile(args[0]);
        Invoker invoker = new Invoker(commandManager);
        BufferedReader readerSystemIn = new BufferedReader(new InputStreamReader(System.in));
        commandManager.register("info", new Info(collectionMeneger));
        commandManager.register("add", new Add(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("show", new Show(collectionMeneger));
        commandManager.register("exit",new Exit());
        commandManager.register("history",new History(commandManager));
        commandManager.register("add_if_min", new AddIfMin(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("help", new Help(commandManager));
        commandManager.register("clear", new Clear(collectionMeneger));
        commandManager.register("remove_by_id",new RemoveById(collectionMeneger));
        commandManager.register("update_id", new UpdateId(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("save", new Save(collectionMeneger));
        commandManager.register("remove_lower", new RemoveLower(collectionMeneger));
        commandManager.register("filter_by_age", new FilterByAge(collectionMeneger));
        commandManager.register("print_field_ascending_name",new PrintFieldAscendingName(collectionMeneger));
        commandManager.register("print_unique_age", new PrintUniqueAge(collectionMeneger));
        commandManager.register("execute_script", new ExecuteScript(invoker,commandManager,collectionMeneger,printer));
        commandManager.register("print_name_by_id",new PrintNameById(collectionMeneger));

        printer.println("Введите help для вывода информации о командах");
        int serverPort = 9876;
        int clientPort;
        Request request = null;
        try (DatagramChannel serverChannel = DatagramChannel.open(); DatagramSocket ds = new DatagramSocket()){

            serverChannel.socket().bind(new InetSocketAddress(serverPort)); // Привязываем серверный сокет к порту 9876
            System.out.println("Server started on port" + serverPort);

            ByteBuffer buffer = ByteBuffer.allocate(100024);

            while (true) {
                buffer.clear();
                System.out.println(1);
                InetSocketAddress clientAddress = (InetSocketAddress) serverChannel.receive(buffer); // Ждем получение пакета от клиента
                System.out.println(2);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                ObjectInputStream ois = new ObjectInputStream(bais);
                request = (Request)ois.readObject();
                System.out.println(request.getCommandName());
                if(Objects.equals(request.getCommandName(), "get_commands"))
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(commandsPack);
                    byte[] sendData = baos.toByteArray();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress.getAddress(), request.getPort());
                    ds.send(sendPacket);
                    System.out.println(5);
                }
                else {
                    String output = invoker.invoke(request);
                    System.out.println(output);
                    byte[] sendData = output.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress.getAddress(), request.getPort());
                    ds.send(sendPacket);
                    System.out.println(6);
                }



            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
