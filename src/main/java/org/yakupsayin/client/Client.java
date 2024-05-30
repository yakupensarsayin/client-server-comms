package org.yakupsayin.client;

import com.squareup.moshi.Moshi;
import org.yakupsayin.Communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client extends Thread{

    private final String username;
    private final Moshi moshi;
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private boolean isListening = true;

    public Client(String name){
        username = name;
        moshi = createMoshi();
        connect();
        start(); // This start comes from our class being a thread.
    }

    @Override
    public void run(){
        // We send a request to join the server with the username specified by the client.
        JoinGameRequest gameRequest = new JoinGameRequest(username);
        String json = createJoinRequestJson(gameRequest);
        Communications.writeMessage(output, json);

        /*
            While waiting for the message, if our username is not unique,
            the server will close our socket and our program will close.
         */
        String response = readMessage();

        while (isListening){

        }
    }

    /**
     * It reads the message coming to InputStream and converts it to String.
     * @return Message that came.
     */
    public String readMessage(){
        try {
            // We allocate an array of 1024 bytes.
            byte[] messageByte = new byte[8192];

            // We get the number of bytes read by InputStream.
            int bytesRead = input.read(messageByte);

            /*
                Using the byte array from messageByte,
                we create a String with as many bytes as the number of bytes read.
                For multi-language support, we convert to UTF-8
                (maybe the user's name contains non-Standard ASCII characters).
            */
            return new String(messageByte, 0, bytesRead, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.out.println("An error was encountered while trying to read a message: " + e.getMessage());
            closeEverything();
            return null;
        }
    }

    /**
     * It sends the given String expression to the socket(s) it addresses.
     * @param message The message to be sent to the socket.
     */
    public void writeMessage(String message){
        try {
            byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
            output.write(messageByte);
            output.flush();
        } catch (IOException e) {
            System.out.println("An error was encountered while trying to write a message: " + e.getMessage());
            closeEverything();
        }
    }

    private void connect(){
        try (Socket clientSocket = new Socket("localhost", 9000)){
            socket = clientSocket;
            input = socket.getInputStream();
            output = socket.getOutputStream();
        } catch (IOException e){
            System.out.println("An error occurred while trying to connect to the server: " + e.getMessage());
            closeEverything();
        }
    }

    private Moshi createMoshi(){
        return new Moshi.Builder().build();
    }

    private String createJoinRequestJson(JoinGameRequest gameRequest){
        return moshi.adapter(JoinGameRequest.class).toJson(gameRequest);
    }
    private void closeEverything(){
        try {
            isListening = false;

            if (socket != null){
                socket.close();
            }

            if(input != null){
                input.close();
            }

            if(output != null){
                output.close();
            }

            interrupt(); // This interrupt comes from our class being a thread.
        } catch (IOException e) {
            System.out.println("An error occurred while trying to close everything: " + e.getMessage());
        }
    }
}