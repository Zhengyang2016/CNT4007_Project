import java.io.IOException;
import java.util.*;
import peerProcess.*;
public class test {
	public static void main(String[] args) {
		
		int spareBits = 3;
		int bitfieldSize = 4;
		byte[] fullBitfield = new byte[bitfieldSize];
		
		System.out.println(fullBitfield[0] + " "+fullBitfield[1] + " "+fullBitfield[2] + " "+fullBitfield[3]);
		Arrays.fill(fullBitfield, (byte)-1);
		System.out.println(fullBitfield[0] + " "+fullBitfield[1] + " "+fullBitfield[2] + " "+fullBitfield[3]);
		
		for(int i = 0;i<=spareBits;++i)
		{
			fullBitfield[bitfieldSize-1] &= (((byte)-1) << (byte)i) ;
		}
		
		System.out.println(fullBitfield[0] + " "+fullBitfield[1] + " "+fullBitfield[2] + " "+fullBitfield[3]);
		
		byte test = (byte) ( -1 << spareBits );
		System.out.println(test);
		
		BitFieldHandler.test();
	}
}
