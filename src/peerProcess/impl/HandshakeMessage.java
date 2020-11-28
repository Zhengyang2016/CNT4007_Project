package peerProcess.impl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import peerProcess.HandshakeMessageI;

/*
 * HandshakeMessage class implements HandshakeMessageI interface
 */

public class HandshakeMessage  {
	static String header = "P2PFILESHARINGPROJ";
	static String zeros = "0000000000";
	static String peerID;
	
	/*
	 * get object of handshakeMessage
	 * get Header,zeros, and peerID
	 */
	
	public static  HandshakeMessage getInstance(){
		HandshakeMessage handshakeMessage = new HandshakeMessage();
		
		return handshakeMessage;
	}
	public static String getHeader() {
		return header;
	}
	
	public String setHeader(String header) {
		
		return this.header;
	}
	
	
	public static String getZeros() {
		return zeros;
	}
	
	public String setZeros(String zeros) {
		
		return this.zeros;
	}
	
	public static String getPeerID() {
		return peerID;
	}
	
	public  String setPeerID(String peerID) {
		
		return this.peerID;
	}
}
