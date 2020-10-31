package demo09_oopPeer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class peerProcess {
	public static void main(String[] args) throws IOException {

		int portNum;
		String hostName;
		int NumberOfPreferredNeighbors;
		int UnchokingInterval;
		int OptimisticUnchokingInterval;
		String FileName;
		int FileSize;
		int PieceSize;
		String peerID = args[0];
		int peerIndex;//the position of this peer in the configuration file

		ArrayList<String[]> peerInfo = new ArrayList<String[]>();

		//reading configuration file
		Config cfg = new Config();//pass command line input to read config.
		NumberOfPreferredNeighbors = cfg.NumberOfPreferredNeighbors;
		System.out.println(NumberOfPreferredNeighbors);
		UnchokingInterval = cfg.UnchokingInterval;
		System.out.println(UnchokingInterval);
		OptimisticUnchokingInterval = cfg.OptimisticUnchokingInterval;
		System.out.println(OptimisticUnchokingInterval);
		FileName = cfg.FileName;
		System.out.println(FileName);
		FileSize = cfg.FileSize;
		System.out.println(FileSize);
		PieceSize = cfg.PieceSize;
		System.out.println(PieceSize);

		peerInfo = cfg.readPeerInfo(peerID);
		System.out.println(peerInfo.get(2)[1]);
		peerIndex = cfg.getIndex();
		System.out.println(peerIndex);

		//portNum = getConnection(peerID, portNum);
		for (int peerNum = peerIndex; peerNum >= 1; peerNum--){
			//Read host name and port number
			hostName = peerInfo.get(peerNum-1)[1];
			System.out.println(hostName);
			//Convert Str to Int
			portNum = Integer.parseInt(peerInfo.get(peerNum-1)[2]);
			System.out.println(portNum);
			Socket peer = new Socket(hostName,portNum);

			new Thread(new Send(peer)).start();
			new Thread(new Receive(peer)).start();
		}

		portNum = Integer.parseInt(peerInfo.get(peerIndex)[2]);
		System.out.println(portNum);
		ServerSocket serverSocket = new ServerSocket(portNum);

		while (true) {
			Socket server = serverSocket.accept();
			System.out.println("Get one connection");

			new Thread(new Send(server)).start();
			new Thread(new Receive(server)).start();
		}
		
	}

	//Try to OOP, unused
	private static int getConnection(String peerID, int portNum) throws IOException {
		for (int peerNum = Integer.parseInt(peerID); peerNum >= 2; peerNum--){
			Socket peer = new Socket("localhost",portNum);

			new Thread(new Send(peer)).start();
			new Thread(new Receive(peer)).start();

			portNum++;
		}
		return portNum;
	}
}
