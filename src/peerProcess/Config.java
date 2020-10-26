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
	public int peerIndex;
	//use arraylist to store peerinfo, each element is a string array of length 4
	public ArrayList<String[]> peerInfo = new ArrayList<String[]>();
	
	public Config() {
		try {
		File common = new File("src/Common.cfg");
		Scanner sc = new Scanner(common);
		String data = sc.nextLine();//read next line
		
		NumberOfPreferredNeighbors = Integer.parseInt(data.split("\\s+")[1]);//split strings with spaces
		data = sc.nextLine();
		UnchokingInterval = Integer.parseInt(data.split("\\s+")[1]);
		data = sc.nextLine();
		OptimisticUnchokingInterval = Integer.parseInt(data.split("\\s+")[1]);
		data = sc.nextLine();
		FileName = data.split("\\s+")[1];
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
	public ArrayList<String[]> readPeerInfo(String peerID) {
		try {
			File peer = new File("src/PeerInfo.cfg");
			Scanner sc = new Scanner(peer);
			int i = 0;
			String data;
			while(sc.hasNextLine()) {
				data = sc.nextLine();
				peerInfo.add(data.split("\\s+"));
				if(data.split("\\s+")[0].equals(peerID))
					this.peerIndex = i;//determine the position of this peer in the configuration file
				++i;
			}
			sc.close();
			return(peerInfo);
		}catch(FileNotFoundException e){
			System.out.println("FileNotFoundException.");
			return(peerInfo);
		}
	}
	
	public int getIndex() {
		return peerIndex;
	}
	
}
