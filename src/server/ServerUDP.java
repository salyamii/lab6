package server;

import server.collection_methods.*;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class ServerUDP extends Thread{
    private DatagramSocket socket;
    HashMap<String, SimpleMethod> option = new HashMap<>();

    public ServerUDP(CollectionAdministrator administrator){
        try{
            socket = new DatagramSocket(4242);
            socket.setSoTimeout(10000);
        }
        catch (SocketException socketException){
            System.out.println("Something wrong with socket you chosen.");
        }
        option.put("help", new Help(administrator));
        option.put("info", new Info(administrator));
        option.put("show", new Show(administrator));
        option.put("insert", new Insert(administrator));
        option.put("update_id", new UpdateID(administrator));
        option.put("remove_key", new RemoveKey(administrator));
        option.put("clear", new Clear(administrator));
        option.put("execute_script", new ExecuteScript(administrator));
        option.put("remove_greater", new RemoveGreater(administrator));
        option.put("remove_greater_key", new RemoveGreaterKey(administrator));
        option.put("remove_lower_key", new RemoveLowerKey(administrator));
        option.put("group_counting_by_population", new GroupCountingByPopulation(administrator));
        option.put("count_by_establishment_date", new CountByEstablishmentDate(administrator));
        option.put("count_less_than_establishment_date", new CountLessThanEstablishmentDate(administrator));
        option.put("exit", new Exit(administrator));
    }

    public void run (){
        System.out.println("Server is online.");
        boolean running = true;
        while(running){
            byte[] bufReceive = new byte[65535];
            DatagramPacket packet = new DatagramPacket(bufReceive, bufReceive.length);
            try{
                socket.receive(packet);
            }
            catch(SocketTimeoutException socketTimeoutException){
                System.out.println("Waiting for a response...");
                continue;
            }catch (IOException ioException){
                System.out.println("Invalid Object received.");
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(bufReceive, bufReceive.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());
            String[] received_arg = received.trim().split(" ", 2);
            String sent;
            if (received_arg[0].equals("exit")){
                sent = option.get(received_arg[0]).run();
                running = false;
                System.out.println("Server is offline.");
            }
            else
                sent = (received_arg.length == 1) ? option.get(received_arg[0]).run()
                    : option.get(received_arg[0]).run(received_arg[1]);
            byte[] bufSend = sent.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(bufSend, bufSend.length, address, port);
            try{
                socket.send(sendPacket);
            }
            catch(IOException ioException){
                System.out.println("Invalid Object sent.");
            }
        }
        socket.close();
    }
}
