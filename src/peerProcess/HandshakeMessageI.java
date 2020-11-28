package peerProcess;

import java.net.Socket;

import peerProcess.impl.HandshakeMessage;

public interface HandshakeMessageI {
	
	void startHandshake(Socket socket);
	
	
	HandshakeMessage getInstance();
	
	String getPeerID();
	
	String setPeerID(String peerID);
}
