package starthack.fridgetogo;

import java.io.*;
import java.net.*;

public class UDPClient {
    private final String ip_address;
    private final int port;
    private DatagramSocket clientSocket;

    public UDPClient(String address, int new_port) throws SocketException {
        ip_address = address;
        port = new_port;
    }

    public void sendMessage(String message) throws Exception {
        clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(ip_address);
        byte[] sendData = new byte[1024];
        sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData,
                sendData.length, IPAddress, port);
        clientSocket.send(sendPacket);
        clientSocket.close();
    }

    public void close(){
        clientSocket.close();
    }
}