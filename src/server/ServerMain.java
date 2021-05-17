package server;

import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Server starts running...");
        try{
            ServerUDP serverUDP = new ServerUDP(new CollectionAdministrator(args[0]));
            serverUDP.run();
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){
            for( ; ; ){
                System.out.print("Enter a correct path to the XML file: ");
                Scanner in = new Scanner(System.in);
                String path = in.nextLine();
                ServerUDP serverUDP = new ServerUDP(new CollectionAdministrator(path));
                serverUDP.run();
            }

        }


    }
}
