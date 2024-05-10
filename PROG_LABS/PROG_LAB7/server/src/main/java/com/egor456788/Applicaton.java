package com.egor456788;




import com.egor456788.commands.*;
import com.egor456788.menegers.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.*;

public class Applicaton {

    private static final int bufSize = 1024*64;
    public static CommandsPack commandsPack = new CommandsPack(new  ArrayList<String>(Arrays.asList(new String[]{"update_id","add","add_if_min"})));
    public Invoker invoker = null;
    public void run(String[] args) {
        Printer printer = new Printer();
        CommandManager commandManager = new CommandManager();
        CollectionMeneger collectionMeneger = new CollectionMeneger();
        collectionMeneger.addFromFile(args[0]);
        invoker = new Invoker(commandManager);
        BufferedReader readerSystemIn = new BufferedReader(new InputStreamReader(System.in));
        commandManager.register("info", new Info(collectionMeneger));
        commandManager.register("add", new Add(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("show", new Show(collectionMeneger));
        commandManager.register("exit",new Exit(collectionMeneger,"output.xml"));
        commandManager.register("history",new History(commandManager));
        commandManager.register("add_if_min", new AddIfMin(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("help", new Help(commandManager));
        commandManager.register("clear", new Clear(collectionMeneger));
        commandManager.register("remove_by_id",new RemoveById(collectionMeneger));
        commandManager.register("update_id", new UpdateId(collectionMeneger,printer, readerSystemIn, false));
        commandManager.register("remove_lower", new RemoveLower(collectionMeneger));
        commandManager.register("filter_by_age", new FilterByAge(collectionMeneger));
        commandManager.register("print_field_ascending_name",new PrintFieldAscendingName(collectionMeneger));
        commandManager.register("print_unique_age", new PrintUniqueAge(collectionMeneger));
        commandManager.register("execute_script", new ExecuteScript(invoker,commandManager,collectionMeneger,printer));
        commandManager.register("print_name_by_id",new PrintNameById(collectionMeneger));

        printer.println("Введите help для вывода информации о командах");
        int serverPort = 9875;
        int clientPort;
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        Request request = null;
        try (DatagramChannel serverChannel = DatagramChannel.open(); DatagramSocket ds = new DatagramSocket()){

            serverChannel.socket().bind(new InetSocketAddress(serverPort));
            Selector selector = Selector.open();
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("Server started on port" + serverPort);

            ExecutorService readExecutorService = Executors.newCachedThreadPool(new PriorityThreadFactory(Thread.MAX_PRIORITY));
            ForkJoinPool executeExecutorService = new ForkJoinPool();
            ForkJoinPool sendExecutorService = new  ForkJoinPool();

            class SendTask implements Runnable {

                final byte[] sendData;
                final Request request;
                final InetSocketAddress clientAddress;

                private SendTask(byte[] sendData, Request request, InetSocketAddress clientAddress) {
                    this.sendData = sendData;
                    this.request = request;
                    this.clientAddress = clientAddress;
                }

                @Override
                public void run() {

                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress.getAddress(), request.getPort());
                        ds.send(sendPacket);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            class ExecuteTask implements Runnable {
                final Request request;
                final InetSocketAddress clientAddress;

                private ExecuteTask(Request request, InetSocketAddress clientAddress) {
                    this.request = request;
                    this.clientAddress = clientAddress;
                }

                @Override
                public void run() {
                    byte[] sendData;
                    try {
                        if (Objects.equals(request.getCommandName(), "get_commands")) {
                            CommandsPack output = commandsPack;
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(output);
                             sendData = baos.toByteArray();
                        } else {
                            String output;
                            if (request.getCommandName().contains("alter")) {
                                output = "Ещё раз введёшь системную комманду и я тебя забаню";
                            } else {
                                output = invoker.invoke(request);
                            }

                             sendData = output.getBytes();
                            System.out.println(output);

                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    sendExecutorService.execute(new SendTask(sendData,request,clientAddress));
                }
            }





            class ReadTask implements Runnable{
                final DatagramChannel channel;

                private ReadTask(DatagramChannel channel) {
                    this.channel = channel;
                }

                @Override
                public void run() {
                    ByteBuffer buffer = ByteBuffer.allocate(bufSize);
                    try {
                        InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                        ObjectInputStream ois;
                        ois = new ObjectInputStream(bais);
                        Request request = (Request) ois.readObject();
                        System.out.println(request.getCommandName());
                        executeExecutorService.execute(new ExecuteTask(request,clientAddress));
                        buffer.clear();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }

            }

            while (true) {




                int count = selector.select();
                if (count == 0){
                    continue;
                }
                Set keySet = selector.selectedKeys();
                Iterator iter = keySet.iterator();
                while (iter.hasNext()) {
                    SelectionKey selectionKey = (SelectionKey) iter.next();
                    if (selectionKey.isReadable()) {
                        System.out.println("Reading channel");
                        readExecutorService.execute(new ReadTask((DatagramChannel) selectionKey.channel()));

                    }
                    iter.remove();

                }
                Thread.sleep(1);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    class PriorityThreadFactory implements ThreadFactory {
        private final int priority;

        public PriorityThreadFactory(int priority) {
            this.priority = priority;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setPriority(priority);
            return t;
        }
    }
    }

