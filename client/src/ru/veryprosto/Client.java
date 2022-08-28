package ru.veryprosto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client implements TCPConnectionListener {
    private static final String IP_ADDR = "localhost";
    private static final int PORT = 8000;
    private TCPConnection connection;


    public static void main(String[] args) {
        Client client = new Client();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String message;
            do {
                message = br.readLine();
                client.connection.sendString(message);
            } while (!message.equals("0"));
            client.connection.disconnect();
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }

    }

    public Client() {
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnct(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception: " + e);
    }

    private static void printMsg(String msg) {
        System.out.println(msg);
    }
}
