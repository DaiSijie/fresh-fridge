package starthack.fridgetogo;

import java.io.*;
import java.net.*;

public class UDPServer {
    private final int port;
    private final DatagramSocket serverSocket;

    public UDPServer(int new_port) throws SocketException {
        port = new_port;
        serverSocket = new DatagramSocket(port);
    }

    public String getPacket() throws Exception {

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData,
                receiveData.length);
        serverSocket.receive(receivePacket);
        String sentence = new String(receivePacket.getData());
        String finalSentence = sentence.substring(0, sentence.indexOf('\0'));
        return finalSentence;
    }

    public void close(){
        serverSocket.close();
    }

}



