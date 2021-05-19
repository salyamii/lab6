package client;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("Starting..");
        ClientUDP clientUDP = new ClientUDP();
        System.out.println("yo");
        clientUDP.sendClient();
        System.out.println("yo");
    }
}
