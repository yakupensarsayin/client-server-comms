package org.yakupsayin.client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        askForUsername();
        String name = getNameFromConsole();
        Client client = new Client(name);
    }

    private static void askForUsername(){
        System.out.print("Select username: ");
    }

    private static String getNameFromConsole(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
