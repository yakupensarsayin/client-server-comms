package org.yakupsayin.server;

public class PlayerJoinedNotification {
    private final String messageType = "PlayerJoinedNotification";
    private String newPlayerName;
    private int numPlayers;

    public PlayerJoinedNotification(String newPlayerName, int numPlayers){
        this.newPlayerName = newPlayerName;
        this.numPlayers = numPlayers;
    }
}
