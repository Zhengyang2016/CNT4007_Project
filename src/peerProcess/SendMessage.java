package peerProcess;

import java.io.DataOutputStream;
import java.io.IOException;

public class SendMessage {
	
	public DataOutputStream out;
	static public byte[] choke = {0,0,0,1,0};
	static public byte[] unchoke = {0,0,0,1,1};
	static public byte[] interested = {0,0,0,1,2};
	static public byte[] notInterested = {0,0,0,1,3};
	static public byte[] have = {0,0,0,5,4};
	static public byte[] bitfield = {5};
	static public byte[] request = {0,0,0,5,6};
	static public byte[] pieceType = {7};
	static String handshake = "P2PFILESHARINGPROJ          ";
	
	
	//create object using socket output stream
	SendMessage(DataOutputStream out){
		this.out = out;
	}
	
	static public void sendHandshake(DataOutputStream dos, String peerID) {
		try {
			dos.writeBytes(handshake);
			dos.writeBytes(peerID);
			dos.flush();
		}catch(IOException e) {
			System.out.println("Send handshake IOException.");
		}
	}
	
	static public void sendChoke(DataOutputStream dos) {
		try {
			dos.write(choke);
			dos.flush();
		}catch(IOException e) {
			System.out.println("Send choke IOException.");
		}
	}
	
	static public void sendUnchoke(DataOutputStream dos) {
		try {
			dos.write(unchoke);
			dos.flush();
		}catch(IOException e) {
			System.out.println("Send unchoke IOException.");
		}
	}
	
	static public void sendInterested(DataOutputStream dos) {
		try {
			dos.write(interested);
			dos.flush();
		}catch(IOException e) {
			System.out.println("Send interested IOException.");
		}
	}
	
	static public void sendNotInterested(DataOutputStream dos) {
		try {
			dos.write(notInterested);
		}catch(IOException e) {
			System.out.println("Send notInterested IOException.");
		}
	}
	
	static public void sendHave(DataOutputStream dos, int pieceIndex) {
		try {
			dos.write(have);
			dos.writeInt(pieceIndex);
			dos.flush();
		}catch(IOException e) {
			System.out.println("Send have IOException.");
		}
	}
	
	static public void sendBitfield(DataOutputStream dos, byte[] bits) {
		try {
			dos.writeInt(bits.length + 1);
			dos.write(bitfield);
			dos.write(bits);
			dos.flush();
		}catch(IOException e) {
			System.out.println("Send bitfield IOException.");
		}
	}
	
	static public void sendRequest(DataOutputStream dos, int pieceIndex) {
		try {
			dos.write(request);
			dos.writeInt(pieceIndex);
			dos.flush();
		}catch(IOException e) {
			System.out.println("Send request IOException.");
		}
	}
	
	static public void sendPiece(DataOutputStream dos, int pieceIndex, byte[] piece) {
		try {
			dos.writeInt(piece.length+5);
			dos.write(pieceType);
			dos.writeInt(pieceIndex);
			dos.write(piece);
			dos.flush();
		}catch(IOException e) {
			System.out.println("Send piece IOException.");
		}
	}
	
}
