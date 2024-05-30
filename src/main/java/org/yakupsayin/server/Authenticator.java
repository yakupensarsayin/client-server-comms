package org.yakupsayin.server;

import java.util.concurrent.CopyOnWriteArrayList;

public class Authenticator {

    public static Boolean canUserLogin(String username, CopyOnWriteArrayList<ClientHandler> clients){

        // Among all client handlers:
        for (ClientHandler client : clients){

            // If the username of any client is equal to the username trying to log in:
            if (client.getUsername().equals(username)){
                return false;
            }
        }

        // If there is no such username among all client handlers:
        return true;
    }
}
