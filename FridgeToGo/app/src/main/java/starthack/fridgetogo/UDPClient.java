package starthack.fridgetogo;

import java.io.*;
import java.net.*;

public class UDPClient {
    private final String ip_address;
    private final int port;
    private final DatagramSocket clientSocket;

    public UDPClient(String address, int new_port) throws SocketException {
        ip_address = address;
        port = new_port;
        clientSocket = new DatagramSocket();
    }

    public void sendMessage(String message) throws Exception {
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
