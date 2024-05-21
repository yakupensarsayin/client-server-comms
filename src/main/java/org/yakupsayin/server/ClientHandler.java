package org.yakupsayin.server;

import com.squareup.moshi.Moshi;
import org.yakupsayin.Communications;
import org.yakupsayin.client.JoinGameRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler extends Thread{
    private static CopyOnWriteArrayList<ClientHandler> clients;

    private Socket socket;
    private InputStream input;
    private OutputStream output;

    public Boolean isListening = true;

    public ClientHandler(CopyOnWriteArrayList<ClientHandler> clients, Socket clientSocket){
        ClientHandler.clients = clients;
        socket = clientSocket;

        try {
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
        }catch (IOException e){
            disconnect();
        }
    }

    @Override
    public void run(){
        try {

            Moshi moshi = new Moshi.Builder().build();

            while (this.isListening){
                String message = Communications.readMessage(input);

                JoinGameRequest gameRequest = moshi.adapter(JoinGameRequest.class).fromJson(message);
            }

        }catch (IOException e){

        }
    }

    private void disconnect(){
        try {
            isListening = false;

            if(socket != null){
                socket.close();
            }

            if(input != null){
                input.close();
            }

            if(output != null){
                output.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
