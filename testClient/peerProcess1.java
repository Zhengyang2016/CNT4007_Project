package testClient;
import peerProcess.Config;
import peerProcess.Utils;
import peerProcess.Send;
import peerProcess.Receive;

import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class peerProcess1 {
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
		System.out.println("NumberOfPreferredNeighbors: "+NumberOfPreferredNeighbors);
		UnchokingInterval = cfg.UnchokingInterval;
		System.out.println("UnchokingInterval: "+UnchokingInterval);
		OptimisticUnchokingInterval = cfg.OptimisticUnchokingInterval;
		System.out.println("OptimisticUnchokingInterval: " + OptimisticUnchokingInterval);
		FileName = cfg.FileName;
		System.out.println("FileName: " + FileName);
		FileSize = cfg.FileSize;
		System.out.println("FileSize: " + FileSize);
		PieceSize = cfg.PieceSize;
		System.out.println("PieceSize: "+ PieceSize);
		//read peers' information and store in peerInfo Arraylist
		peerInfo = cfg.readPeerInfo(peerID);
		System.out.println(peerInfo.get(0)[1]);
		peerIndex = cfg.getIndex();
		System.out.println("Index of this peer: " + peerIndex);
		
		//initialize the bitfield of this peer		
		int bitfieldSize = FileSize/PieceSize;
		if(FileSize%PieceSize != 0)
			bitfieldSize += 1;	
		if(bitfieldSize % 8 != 0)
			bitfieldSize = bitfieldSize/8 + 1;
		else
			bitfieldSize = bitfieldSize/8;
		byte[] bitfield = new byte[bitfieldSize];

		System.out.println("\nConnecting peers...");
		//portNum = getConnection(peerID, portNum);
		for (int peerNum = peerIndex; peerNum >= 1; peerNum--){
			//Read host name and port number
			hostName = peerInfo.get(peerNum-1)[1];
			System.out.println(hostName);
			//Convert Str to Int
			portNum = Integer.parseInt(peerInfo.get(peerNum-1)[2]);
			System.out.println(portNum);
			Socket peer = new Socket(hostName,portNum);

			//new Thread(new Send(peer)).start();
			new Thread(new Receive(peer, peerInfo.get(peerNum-1)[0])).start();
		}

		portNum = Integer.parseInt(peerInfo.get(peerIndex)[2]);
		System.out.println("\nWaiting for connections on port " + portNum  + "..." );
		ServerSocket serverSocket = new ServerSocket(portNum);
		
		while (true) {
			Socket server = serverSocket.accept();
			System.out.println("Get one connection");

			//new Thread(new Send(server)).start();
			new Thread(new Receive(server,peerInfo.get(peerIndex+1)[0])).start();
			peerIndex++;
		}
		
	}

	//Try to OOP, unused
	/*private static int getConnection(String peerID, int portNum) throws IOException {
		for (int peerNum = Integer.parseInt(peerID); peerNum >= 2; peerNum--){
			Socket peer = new Socket("localhost",portNum);

			new Thread(new Send(peer)).start();
			new Thread(new Receive(peer)).start();

			portNum++;
		}
		return portNum;
	}*/
}	