package peerProcess;

public class MyStats {
	public String peerID;
	public byte[] bitfield;
	public byte sparebits;
	public boolean downloadFinished;
	
	public MyStats(String peerID, byte[] bitfield,boolean download, byte sparebits) {
		this.peerID = peerID;
		this.bitfield = bitfield;
		this.downloadFinished = download;
		this.sparebits = sparebits;
		
	}
	
	
}
