package demo09_oopPeer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;

public class Peer2 {
    public static void main(String[] args) throws IOException {
        int peerID = 2;
        int portNum = 6666;

        portNum = getConnection(peerID, portNum);

        ServerSocket serverSocket = new ServerSocket(portNum);

        while (true) {
            Socket server = serverSocket.accept();
            System.out.println("Get one connection");

            new Thread(new Send(server)).start();
            new Thread(new Receive(server)).start();
        }
    }

    private static int getConnection(int peerID, int portNum) throws IOException {
        for (int peerNum = peerID; peerNum >= 2; peerNum--){
            Socket peer = new Socket("localhost",portNum);

            new Thread(new Send(peer)).start();
            new Thread(new Receive(peer)).start();

            portNum++;
        }
        return portNum;
    }
}
