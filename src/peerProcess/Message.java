package peerProcess;

import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Message {
	public InputStream in;
	//call readNext() before using these data
	public int length;
	public int type;
	public int have;
	public byte[] bitfield;
	public int request;
	public int pieceIndex;
	public byte[] piece;
	
	//constructer needs the socket inputstream.
	Message(InputStream in){
		this.in = in;
	}
	
	//read handshake message
	public String readHandshake() {
		int read;//used to keep track of next byte read
		byte[] bytes = new byte[18];
		
		try {
			for(int i = 0;i<18;++i) {
				read = in.read();
				if(read != -1)//make sure read() does not return -1
					bytes[i] = (byte)read;
			}
			String h = new String(bytes);
			if(!h.equals("P2PFILESHARINGPROJ"))
			{
				System.out.println("Header not correct: " + h);
				throw new IOException("Exception thrown");
			}
			if (in.skip(10) == 10)
			{
				bytes = new byte[4];
				for(int i = 0;i<4;++i) {
					read = in.read();
					if(read != -1)//make sure read() does not return -1
						bytes[i] = (byte)read;
				}
				String peerID = new String(bytes);
				System.out.println("PeerID of handshake: " + peerID);
				return peerID;
			}
			else
			{
				System.out.println("Failed to read 10 zero bytes.");
				throw new IOException("Exception thrown");
			}
		}catch(IOException e) {
			System.out.println("Get handshake message failed.");
			return null;
		}
	}
	
	//read next message
	public void readNext() throws IOException{
		int read;//used to keep track of next byte read
		byte[] bytes = new byte[4];
		
		//get message length
		try {
			for(int i = 0;i<4;++i) {
				read = in.read();
				if(read != -1)//make sure read() does not return -1
					bytes[i] = (byte)read;
			}
			length = ByteBuffer.wrap(bytes).getInt();//convert 4 bytes to integer
			System.out.print("Message length: " + length);
		}catch(IOException e) {
			System.out.println("Get message length failed.");
			throw e;
		}
		
		//get message type
		try {
			read = in.read();
			if(read != -1)
				type = read;
			System.out.print(" Type: " + type);
		}catch(IOException e) {
			System.out.println("Get message type failed.");
		}
		
		//"have" message
		if(type == 4) {
			try {
				for(int i = 0;i<4;++i){
					read = in.read();
					if(read != -1)
						bytes[i] = (byte)read;
				}
				have = ByteBuffer.wrap(bytes).getInt();
				System.out.println("  Have index: " + have);
			}catch(IOException e) {
				System.out.println("Get message have piece index failed.");
			}
		}
		
		//"bitfield" message
		if(type == 5) {
			bitfield = new byte[length -1];
			try {
				for(int i = 0;i<length-1;++i){
					read = in.read();
					if(read != -1)
						bitfield[i] = (byte)read;
				}
				System.out.println("  Bitfield read.");
			}catch(IOException e) {
				System.out.println("Get message bitfield failed.");
			}
		}
		
		//"request" message
		if(type == 6) {
			try {
				for(int i = 0;i<4;++i){
					read = in.read();
					if(read != -1)
						bytes[i] = (byte)read;
				}
				request = ByteBuffer.wrap(bytes).getInt();
				System.out.println("  Request index: " + request);
			}catch(IOException e) {
				System.out.println("Get message request piece index failed.");
			}
		}
		
		//"piece" message
		if(type == 7) {
			try {
				for(int i = 0;i<4;++i){
					read = in.read();
					if(read != -1)
						bytes[i] = (byte)read;
				}
				pieceIndex = ByteBuffer.wrap(bytes).getInt();
				System.out.print(" Piece index: " + pieceIndex);
				piece = new byte[length - 5];
				for(int i = 0;i<length-5;++i) {
					read = in.read();
					if(read != -1)
						piece[i] = (byte)read;
				}
				System.out.println("  Piece content read.");
			}catch(IOException e) {
				System.out.println("Get message piece payload failed.");
			}
		}
		
	}
	

	
}
