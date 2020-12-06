package peerProcess;

//import handler.BitFieldHandler;
/*
 * This class: 
 * 1. set the bits in the bitfield(byte[] -> int[])
 * Each bit in the bitfield payload represents 
 * whether the peer has the corresponding piece or not
 * 2. Determine whether interested or not
 * 3. Determine if download is finished
 * 4. int[] -> byte[]
 * 5. get the number pf piece
 */
public class Bitfield {
	
	private int pieceNumber;
	private int[] bitfield;
	
	

	public Bitfield(int pieceNumber) {
		this.pieceNumber = pieceNumber;
		bitfield = new int[pieceNumber];
		for (int i = 0 ; i< pieceNumber; i++) {
			bitfield[i] = 0;
		}
		
	}
	
	/*
	 * convert int[] into byte[]
	 */
	synchronized  byte[] converttoBytes() {
		
		int bytesNumber = pieceNumber/8;
		if(pieceNumber % 8 != 0)
			bytesNumber = pieceNumber/8 + 1;
		
		byte[] bytes = new byte[bytesNumber];
		for (int i = 0; i < bytesNumber; i++) {
			bytes[i] = (byte)0;
		}
		for (int i = 0; i < pieceNumber; i++) {
			
			if (bitfield[i] == 1) {
				bytes[i/8] = (byte) (bytes[i/8] | (1 << i%8));
			} else {
				bytes[i/8] = (byte) (bytes[i/8] & ~(1 << i%8));
			}
		}
		
		return bytes;
	}
	
	
	//set a bit in the bitfield.
	
	synchronized void setBit(int[] a, int index) {
		
		if(a[index] == 0)
			a[index] = 1;
		bitfield[index] = a[index];
	}
	/*
	synchronized void setBit(byte[] bytes, int index) {
		
		bytes[index/8] |= (128 >>> (index % 8));
		//bitfield = BitFieldHandler.toIntArray(bytes);
		for(int i = 0; i<pieceNumber; i++) {
		if((bytes[i/8] & (1 << (i%8)) )== 0){
			bitfield[i] = 0;
		}
		else {
			bitfield[i] = 1;
		}
		}
	}
	*/
	
	/*
	 * calculate pieceNumber
	 * 
	 */
	public int getPieceNumber(int FileSize, int PieceSize) {
		int pieceNumber = FileSize/PieceSize;
		if(FileSize%PieceSize != 0)
			pieceNumber += 1;	
		return pieceNumber;
	}
	
	/*
	 * check if all pieces are donwloaded
	 * 
	 */
	public boolean isDonwloadFinished() {
		
		boolean DownloadFinished = true;
		for(int i =0; i < pieceNumber; i++) {
			if(bitfield[i]== 0)
			{
				DownloadFinished = false;
				
			}
			
		}
		return DownloadFinished;
	}
	
	/*
	 * check if the piece existed in b also existed in this bitfield
	 * if true, return interested
	 * otherwise, return not interested
	 */
	synchronized boolean isInterested(Bitfield neighbor) {
		boolean interested = false;
		for (int i = 0; i < pieceNumber; i++) {
			if ((bitfield[i] == 0) && neighbor.bitfield[i] == 1) {
				interested = true;
			} 
		}
		return interested;
		
	}
}
	
