package starthack.fridgetogo;

import java.io.*;
import java.net.*;

class UDPServer {
    private final int port;

    public UDPServer(int new_port){
        port = new_port;
    }
    public String getPacket() throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(port);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        String sentence = new String(receivePacket.getData());
        String finalSentence = sentence.substring(0,sentence.indexOf('\0'));
        return sentence;
    }

}



