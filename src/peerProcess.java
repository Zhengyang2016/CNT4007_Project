import peerProcess.Config;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class peerProcess {
	public static void main(String[] args) {
		int NumberOfPreferredNeighbors;
		int UnchokingInterval;
		int OptimisticUnchokingInterval;
		String FileName;
		int FileSize;
		int PieceSize;
		
		ArrayList<String[]> peerInfo = new ArrayList<String[]>();
		
		Config cfg = new Config();
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
		
		peerInfo = cfg.readPeerInfo();
		System.out.println(peerInfo.get(5)[0]);
		
		
	}
}
