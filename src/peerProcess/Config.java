package peerProcess;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Config {
	public int NumberOfPreferredNeighbors;
	public int UnchokingInterval;
	public int OptimisticUnchokingInterval;
	public String FileName;
	public int FileSize;
	public int PieceSize;
	//use arraylist to store peerinfo, each element is a string array of length 4
	public ArrayList<String[]> peerInfo = new ArrayList<String[]>();
	
	public Config() {
		try {
		File common = new File("src/Common.cfg");
		Scanner sc = new Scanner(common);
		String data = sc.nextLine();//read next line
		
		NumberOfPreferredNeighbors = Integer.parseInt(data.split("\\s+")[1]);
		data = sc.nextLine();
		UnchokingInterval = Integer.parseInt(data.split("\\s+")[1]);
		data = sc.nextLine();
		OptimisticUnchokingInterval = Integer.parseInt(data.split("\\s+")[1]);
		data = sc.nextLine();
		FileName = data.split("\\s+")[0];
		data = sc.nextLine();
		FileSize = Integer.parseInt(data.split("\\s+")[1]);
		data = sc.nextLine();
		PieceSize = Integer.parseInt(data.split("\\s+")[1]);
		
		sc.close();
		}catch(FileNotFoundException e){
			System.out.println("FileNotFoundException.");
		}
	}

	//	 this method returns the peerInfo arraylist
	public ArrayList<String[]> readPeerInfo() {
		try {
			File peer = new File("src/PeerInfo.cfg");
			Scanner sc = new Scanner(peer);
			while(sc.hasNextLine()) {
				peerInfo.add(sc.nextLine().split("\\s+"));
			}
			sc.close();
			return(peerInfo);
		}catch(FileNotFoundException e){
			System.out.println("FileNotFoundException.");
			return(peerInfo);
		}
	}
	
}
