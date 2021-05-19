package server;

import server.collection_methods.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class ServerUDP extends Thread{
    private DatagramSocket socket;
    private boolean running;
    private byte[] bufReceive = new byte[65535];
    private byte[] bufSend = new byte[65535];
    HashMap<String, SimpleMethod> option = new HashMap<>();

    public ServerUDP(CollectionAdministrator administrator){
        try{
            socket = new DatagramSocket(4242);
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
        running = true;
        while(running){
            DatagramPacket packet = new DatagramPacket(bufReceive, bufReceive.length);
            try{
                socket.receive(packet);
            }
            catch (IOException ioException){
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
            }
            else
                sent = (received_arg.length == 1) ? option.get(received_arg[0]).run()
                    : option.get(received_arg[0]).run(received_arg[1]);
            bufSend = sent.getBytes();
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
