package org.yakupsayin.server;

import com.squareup.moshi.Moshi;
import org.yakupsayin.Communications;
import org.yakupsayin.client.JoinGameRequest;
import org.yakupsayin.client.PlayerJoinedNotification;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler extends Thread{
    private static CopyOnWriteArrayList<ClientHandler> clients;
    private static int numberOfPlayers = 0;
    private final Socket socket;
    private final Moshi moshi;
    private InputStream input;
    private OutputStream output;
    private String username;
    public Boolean isListening = true;

    public ClientHandler(CopyOnWriteArrayList<ClientHandler> clients, Socket clientSocket){
        ClientHandler.clients = clients;
        socket = clientSocket;
        moshi = createMoshi();
        getStreams();
    }

    @Override
    public void run(){
        // Read the message of the user trying to log in:
        String message = Communications.readMessage(input);

        // Create JoinGameRequest object from that message
        JoinGameRequest gameRequest = createJoinRequestClassFromJson(message);
        assert gameRequest != null;

        // Ask Authenticator if you can log in with this username
        Boolean canLogin = Authenticator.canUserLogin(gameRequest.getPlayerName(), clients);

        // If the user cannot log in, stop the program
        if (!canLogin){
            disconnect();
        }

        username = gameRequest.getPlayerName();
        clients.add(this);
        numberOfPlayers += 1;
        BroadcastUserJoined(username);

        while (isListening){
            String cmd = Communications.readMessage(input);
        }

    }

    private void BroadcastUserJoined(String username){
        PlayerJoinedNotification notification = new PlayerJoinedNotification(username, numberOfPlayers);
        String json = createPlayerJoinedJson(notification);
        for (ClientHandler client : clients){
            Communications.writeMessage(client.getOutput(), json);
        }
    }

    private String createPlayerJoinedJson(PlayerJoinedNotification playerJoinedNotification){
        return moshi.adapter(PlayerJoinedNotification.class).toJson(playerJoinedNotification);
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

            interrupt(); // This interrupt comes from our class being a thread.

            clients.remove(this); // We delete this from client handlers.

            System.out.println("Client has disconnected!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getStreams(){
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
        }catch (IOException e){
            System.out.println("An error was encountered while trying to get streams: " + e.getMessage());
            disconnect();
        }
    }

    private Moshi createMoshi(){
        return new Moshi.Builder().build();
    }

    private JoinGameRequest createJoinRequestClassFromJson(String message){
        try {
            return moshi.adapter(JoinGameRequest.class).fromJson(message);
        } catch (IOException e) {
            disconnect();
            return  null;
        }
    }

    public String getUsername() {
        return username;
    }

    public OutputStream getOutput() {
        return output;
    }

}
