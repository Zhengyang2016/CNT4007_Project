package peerProcess;

import java.io.DataOutputStream;
import java.io.IOException;

public class SendMessage {
	
	public DataOutputStream out;
	public byte[] choke = {0,0,0,1,0};
	public byte[] unchoke = {0,0,0,1,1};
	public byte[] interested = {0,0,0,1,2};
	public byte[] notInterested = {0,0,0,1,3};
	public byte[] have = {0,0,0,5,4};
	public byte[] bitfield = {5};
	public byte[] request = {0,0,0,5,6};
	public byte[] piece = {7};
	
	//create object using socket output stream
	SendMessage(DataOutputStream out){
		this.out = out;
	}
	
	public void sendChoke() {
		try {
			out.write(choke);
		}catch(IOException e) {
			System.out.println("Send choke IOException.");
		}
	}
	
	public void sendUnchoke() {
		try {
			out.write(unchoke);
		}catch(IOException e) {
			System.out.println("Send unchoke IOException.");
		}
	}
	
	public void sendInterested() {
		try {
			out.write(interested);
		}catch(IOException e) {
			System.out.println("Send interested IOException.");
		}
	}
	
	public void sendNotInterested() {
		try {
			out.write(notInterested);
		}catch(IOException e) {
			System.out.println("Send notInterested IOException.");
		}
	}
	
	public void sendHave(int pieceIndex) {
		try {
			out.write(have);
			out.writeInt(pieceIndex);
		}catch(IOException e) {
			System.out.println("Send have IOException.");
		}
	}
	
	public void sendBitfield(byte[] bits) {
		try {
			out.writeInt(bits.length + 1);
			out.write(bitfield);
			out.write(bits);
		}catch(IOException e) {
			System.out.println("Send bitfield IOException.");
		}
	}
	
	public void sendRequest(int pieceIndex) {
		try {
			out.write(request);
			out.writeInt(pieceIndex);
		}catch(IOException e) {
			System.out.println("Send request IOException.");
		}
	}
	
	public void sendPiece(int pieceIndex, byte[] piece) {
		try {
			out.writeInt(piece.length+5);
			out.write(this.piece);
			out.writeInt(pieceIndex);
			out.write(piece);
		}catch(IOException e) {
			System.out.println("Send piece IOException.");
		}
	}
	
}
