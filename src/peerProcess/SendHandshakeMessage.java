package peerProcess;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import peerProcess.impl.HandshakeMessage;

/*
 * this class SendHandshakeMessage provide function of sending handshake
 */

public class SendHandshakeMessage {
	
	boolean hdshakeSent = false;
	
	public void startHandshake(Socket socket){
		try {
			//String message= header + zeros + setpeerID(setPeerID);
			HandshakeMessage message = HandshakeMessage.getInstance();
			message.setHeader(HandshakeMessage.getHeader());
			message.setZeros(HandshakeMessage.getZeros());
			message.setPeerID(HandshakeMessage.getPeerID());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			System.out.println(""+in.readObject());
			hdshakeSent = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Send hdshake Exception.");
			e.printStackTrace();
		}
	}
}
