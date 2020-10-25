package demo01;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HandshakeMessage {
	static String header = "P2PFILESHARINGPROJ";
	static String zeros = "0000000000";
	static String peerID;
	
	
	
	public static void startHandshake(Socket socket){
		try {
			//String message= header + zeros + setpeerID(setPeerID);
			HandshakeMessage message = HandshakeMessage.getInstance();
			message.setPeerID(getPeerID());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			System.out.println(""+in.readObject());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failed TO do Handshake");
			e.printStackTrace();
		}
	}
	
	public static HandshakeMessage getInstance(){
		HandshakeMessage handshakeMessage = new HandshakeMessage();
		
		return handshakeMessage;
	}
	
	public static String getPeerID() {
		return peerID;
	}
	public String setPeerID(String peerID) {
		this.peerID = peerID;
		return peerID;
	}
	
}