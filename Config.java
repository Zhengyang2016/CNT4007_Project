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
	
	public ArrayList<String[]> peerInfo = new ArrayList<String[]>();
	
	public void readCommon() {
		try {
		File common = new File("Common.cfg");
		Scanner sc = new Scanner(common);
		String data = sc.nextLine();
		String[] split = data.split("\\s+");
		NumberOfPreferredNeighbors = Integer.parseInt(split[1]);
		data = sc.nextLine();
		UnchokingInterval = Integer.parseInt(split[1]);
		data = sc.nextLine();
		OptimisticUnchokingInterval = Integer.parseInt(split[1]);
		data = sc.nextLine();
		FileName = split[1];
		data = sc.nextLine();
		FileSize = Integer.parseInt(split[1]);
		data = sc.nextLine();
		PieceSize = Integer.parseInt(split[1]);
		sc.close();
		}catch(FileNotFoundException e){
			System.out.println("FileNotFoundException.");
		}
	}
	
	public ArrayList<String[]> readPeerInfo() {
		try {
			File peer = new File("PeerInfo.cfg");
			Scanner sc = new Scanner(peer);
			while(sc.hasNextLine()) {
				peerInfo.add(sc.nextLine().split("\\s+"));
			}
			sc.close();
		}catch(FileNotFoundException e){
			System.out.println("FileNotFoundException.");
		}finally {
			return(peerInfo);
		}
	}
	
}
