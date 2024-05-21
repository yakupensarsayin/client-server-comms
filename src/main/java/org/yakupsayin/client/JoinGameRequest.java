package org.yakupsayin.client;

public class JoinGameRequest {
    private final String messageType = "JoinGameRequest";
    private final String playerName;

    public JoinGameRequest(String name){
        playerName = name;
    }

    public String getMessageType() {
        return messageType;
    }
    public String getPlayerName() {
        return playerName;
    }
}
