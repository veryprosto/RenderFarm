package ru.veryprosto;

public interface TCPConnectionListener {
    void onConnectionReady(TCPConnection tcpConnection);

    void onReceiveString(TCPConnection tcpConnection, String value);

    void onDisconnct(TCPConnection tcpConnection);

    void onException(TCPConnection tcpConnection, Exception e);
}
