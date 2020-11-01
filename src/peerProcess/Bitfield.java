package peerProcess;

public class Bitfield {
	
	//set a bit in the bitfield.
	public static void setBit(byte[] bytes, int index) {
		bytes[index/8] |= (128 >>> (index % 8)); 
	}
	
	
	
	
}
