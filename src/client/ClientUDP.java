package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import server.data.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.net.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientUDP {
    private DatagramSocket socket;
    private InetAddress address;
    XmlMapper mapper = new XmlMapper();
    private boolean running;
    private final Scanner in = new Scanner(System.in);
    HashSet<String> options = new HashSet<>();
    private byte[] buf = new byte[65535];
    private byte[] bufFromServer = new byte[65535];
    //Utility date formatters
    public static final String DATE_FORMATTER = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);

    public ClientUDP() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException unknownHostException) {
            System.out.println("Can't find IP. Fix it.");
        } catch (SocketException socketException) {
            System.out.println("The socket could not be opened, or the socket could not bind to the specified local port.");
        }
        options.add("help");
        options.add("info");
        options.add("insert");
        options.add("update_id");
        options.add("remove_key");
        options.add("clear");
        options.add("execute_script");
        options.add("exit");
        options.add("remove_greater");
        options.add("remove_greater_key");
        options.add("remove_lower_key");
        options.add("group_counting_by_population");
        options.add("count_by_establishment_date");
        options.add("count_less_than_establishment_date");
    }


    public void sendClient() {
        try{
            running = true;
            while (running) {
                System.out.print("Enter an option: ");
                String option;
                String[] optionSplitted;
                while (true) {
                    option = in.nextLine();
                    optionSplitted = option.trim().split(" ", 2);
                    if (options.contains(optionSplitted[0]))
                        break;
                    System.out.print("Incorrect option! Try help for information.\n Enter an option: ");
                }
                if(optionSplitted[0].equals("exit")){
                    running = false;
                    System.out.println("You exited an application.");
                    socket.close();
                    System.exit(0);
                }
                try{
                    if(optionSplitted[0].equals("insert")){
                        String out = optionSplitted[0] + " " + mapper.writeValueAsString(makeCity());
                        buf = out.getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
                        socket.send(packet);
                    }
                    else if(optionSplitted[0].equals("update_id")){
                        String out = optionSplitted[0] + " " + mapper.writeValueAsString(makeCity());
                        buf = out.getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
                        socket.send(packet);
                    }
                    else{

                        String out = optionSplitted.length == 1 ? optionSplitted[0] : optionSplitted[0] + " " + optionSplitted[1];
                        buf = out.getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
                        socket.send(packet);

                    }
                }
                catch (JsonProcessingException jsonProcessingException){
                    System.out.println("Processing exception when sending..");
                }
                DatagramPacket packet = new DatagramPacket(bufFromServer, bufFromServer.length);
                socket.receive(packet);
                System.out.println("check");
                String whatReceived = new String(packet.getData(), 0, packet.getLength());
                System.out.println(whatReceived);
            }
        }
       catch (IOException ioException){
           System.err.println("The server is not available now. Reconnecting in 5 seconds. \n " +
                   "To disconnect type {N} or press any key to continue.");
           String ans = in.nextLine();
           if(ans.equals("N")){
               System.out.println("Disconnecting now.");
               close();
               System.exit(2);
           }
           try{
               Thread.sleep(5000);
           }
           catch (InterruptedException interruptedException){
               System.out.println("Disconnecting now because of interrupting.");
               close();
               System.exit(1);
           }
           System.out.println("Reconnecting...");
       }
    }


    public void close(){
        socket.close();
    }

    public City updateCity(){
        for( ; ; ){
            try{
                System.out.print("Enter ID of a city: ");
                long id = in.nextInt();
                City city = new City(id, receiveName(), receiveCoordinates(), null,
                        receiveArea(), receivePopulation(), receiveMetersAboveSeaLevel(),
                        receiveEstablishmentDate(), receiveTelephoneCode(), receiveClimate(), receiveGovernor());
                return city;
            }
            catch(InputMismatchException inputMismatchException){
                System.out.println("Enter a long-type value, please.");
            }

        }

    }

    public City makeCity(){
        City city = new City(0, receiveName(), receiveCoordinates(), null,
                receiveArea(), receivePopulation(), receiveMetersAboveSeaLevel(),
                receiveEstablishmentDate(), receiveTelephoneCode(), receiveClimate(), receiveGovernor());
        return city;
    }



    //Receiving methods for making an City object
    /** Method for receiving name
     *
     * @return String name
     */
    public String receiveName(){
        for( ; ; ){
            try{
                Scanner in = new Scanner(System.in);
                System.out.println("Enter a name of a town.");
                String name = in.nextLine().trim();
                if(name.equals("")){
                    System.out.println("This value can't be empty.");
                    continue;
                }
                return name;
            }
            catch (InputMismatchException inputMismatchException){
                System.out.println("This value must be String.");
            }
        }
    }

    /**
     * Method for receiving X coordinate
     * @return float X
     */
    public float receiveX(){
        for( ; ; ){
            try{
                Scanner in = new Scanner(System.in);
                System.out.println("Enter X coordinate in a float type. Value must be greater than -944 and can't be empty.");
                float x = in.nextFloat();
                String strX = Float.toString(x);
                if(x < -944){
                    System.out.println("Value can't be lower than -944.");
                    continue;
                }
                if(strX.equals("")){
                    System.out.println("Value can't be empty.");
                    continue;
                }
                return x;
            }
            catch (InputMismatchException inputMismatchException){
                System.out.println("This value must be a float-number type. Try again.");
            }
        }
    }

    /**
     * Method for receiving y coordinate
     * @return int Y
     */
    public int receiveY(){
        for( ; ; ){
            try{
                Scanner in = new Scanner(System.in);
                System.out.print("Enter Y coordinate in a int type. Value can't be empty.");
                int y = in.nextInt();
                String strY = Integer.toString(y);
                if(strY.equals("")){
                    System.out.println("Value can't be empty.");
                    continue;
                }
                return y;
            }
            catch (InputMismatchException inputMismatchException){
                System.out.println("This value must be a int-number type. Try again.");
            }
        }
    }
    /**
     * Method for receiving Coordinate-type field
     * @return Coordinates coordinates
     */
    public Coordinates receiveCoordinates(){
        return new Coordinates(receiveX(), receiveY());
    }
    /**
     * Method for receiving an area
     * @return double area
     */
    public double receiveArea(){
        for( ; ; ) {
            try {
                System.out.println("Enter a number of area in double format. Value must be more that 0 and can't be empty.");
                Scanner in = new Scanner(System.in);
                double num = in.nextDouble();
                String strNum = Double.toString(num);
                if (num <= 0) {
                    System.out.println("Value must be more that 0.");
                    continue;
                }
                if(strNum.equals("")){
                    System.out.println("Value can't be empty.");
                    continue;
                }
                return num;
            }
            catch(InputMismatchException inputMismatchException){
                System.out.println("The value must be double type. Try again.");
            }
        }
    }

    /**
     * Method for receiving population
     * @return int population
     */
    public int receivePopulation(){
        for( ; ; ){
            try{
                Scanner in = new Scanner(System.in);
                System.out.println("Enter number of population. Value must be greater than 0 and can't be empty.");
                int pop = in.nextInt();
                String strPop = Integer.toString(pop);
                if(pop < 0){
                    System.out.println("Value must be greater than zero.");
                    continue;
                }
                if(strPop.equals("")){
                    System.out.println("Value can't be empty.");
                    continue;
                }
                return pop;
            }
            catch(InputMismatchException inputMismatchException){
                System.out.println("Value must be int-type format.");
            }
        }
    }

    /**
     * Method for receiving meters above sea level
     * @return float metersAboveSeaLevel
     */
    public float receiveMetersAboveSeaLevel(){
        for( ; ; ){
            try{
                Scanner in = new Scanner(System.in);
                System.out.println("Enter meters above sea level. Value can't be empty.");
                float meters = in.nextFloat();
                String strMeters = Float.toString(meters);
                if(strMeters.equals("")){
                    System.out.println("Value can't be empty.");
                    continue;
                }
                return meters;
            }
            catch(InputMismatchException inputMismatchException){
                System.out.println("Value must be float-type. Try again.");
            }
        }
    }

    /**
     * Method for receiving establishment date
     * @return LocalDate establishmentDate
     */
    public LocalDate receiveEstablishmentDate() {
        for (; ; ) {
            try {
                Scanner in = new Scanner(System.in);
                System.out.println("Enter an establishment date in format yyyy-MM-dd.");
                String date = in.nextLine();
                if (date.equals("")) {
                    System.out.println("Date value can't be empty.");
                    continue;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
                LocalDate establishmentDate = LocalDate.parse(date, formatter);
                return establishmentDate;
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Date is a String object. Try again.");
            } catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Invalid date format. Try again.");
            }
        }
    }

    /**
     * Method for receiving telephone code
     * @return int telephoneCode
     */
    public int receiveTelephoneCode(){
        for( ; ; ){
            try{
                Scanner in = new Scanner(System.in);
                System.out.println("Enter a telephone code. Value must be greater than 0, but lower than 100000.");
                int code = in.nextInt();
                String strCode = Integer.toString(code);
                if(strCode.equals("")){
                    System.out.println("Code can't be empty.");
                    continue;
                }
                if(code <= 0){
                    System.out.println("Code must be greater than 0.");
                    continue;
                }
                if(code > 100000){
                    System.out.println("Code must be lower than 100000.");
                    continue;
                }
                return code;
            }catch (InputMismatchException inputMismatchException){
                System.out.println("Value of code must be int-type. Try again.");
            }
        }
    }

    /**
     * Method for receiving a climate
     * @return Climate
     */
    public Climate receiveClimate(){
        for( ; ; ){
            try {
                Scanner in = new Scanner(System.in);
                System.out.println("You need to choose one of the options.");
                System.out.println("1 - Humid continental");
                System.out.println("2 - Monsoon");
                System.out.println("3 - Subarctic");
                System.out.println("4 - Tundra");
                System.out.print("Choose 1, 2, 3 or 4: ");
                int number = in.nextInt();
                switch (number){
                    case 1:
                        return Climate.HUMIDCONTINENTAL;
                    case 2:
                        return Climate.MONSOON;
                    case 3:
                        return Climate.SUBARCTIC;
                    case 4:
                        return Climate.TUNDRA;
                    default:
                        break;
                }
                System.out.println("You should enter 1, 2, 3 or 4. Try again.");
            }
            catch(InputMismatchException inputMismatchException){
                System.out.println("This value must be a number (1, 2, 3, 4). Choose one and try again.");
            }
        }
    }
    /** Method for receiving a governor
     *
     * @return Human governor
     */
    public Human receiveGovernor(){
        for( ; ; ){
            try{
                Scanner in = new Scanner(System.in);
                System.out.println("Enter a date and time of birth with a format: yyyy-MM-dd hh:MM:ss.");
                String dateTime = in.nextLine();
                LocalDateTime birthday = LocalDateTime.parse(dateTime, dateTimeFormatter);
                return new Human(birthday);
            }
            catch(InputMismatchException inputMismatchException){
                System.out.println("Input value must be String format.");
            }
            catch (DateTimeException dateTimeException){
                System.out.println("Your data format is invalid. Try again.");
            }
        }
    }

}
