import peerProcess.Config;
import java.util.ArrayList;

public class peerProcess {
	public static void main(String[] args) {
		
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
		System.out.println(peerInfo.get(3)[0]);
		peerIndex = cfg.getIndex();
		System.out.println(peerIndex);
		
	}
}
