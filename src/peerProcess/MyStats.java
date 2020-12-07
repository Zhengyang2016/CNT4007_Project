package peerProcess;

import java.util.List;

public class MyStats {
	public String peerID;
	public String destDir;
	public String fileName;
	public byte[] bitfield;
	public byte sparebits;
	public volatile boolean downloadFinished;
	public boolean haveFullFile;
	public volatile boolean allPeerFinished = false;
	public List<String> destPaths;
	public int pieceNum;
	public int fileSize;
	public int pieceSize;
	public volatile int numberOfPiece = 0;
	
	public MyStats(String peerID, byte[] bitfield,boolean download,
				   byte sparebits, String fileName, String destDir,
				   List<String> destPaths, int fileSize, int pieceSize, int pieceNum) {

		this.peerID = peerID;
		this.bitfield = bitfield;
		this.downloadFinished = download;
		this.sparebits = sparebits;

		this.haveFullFile = download;
		this.fileName = fileName;
		this.destDir = destDir;
		this.destPaths = destPaths;
		this.fileSize = fileSize;
		this.pieceSize = pieceSize;
		this.pieceNum = pieceNum;

		
	}
	
	
}
