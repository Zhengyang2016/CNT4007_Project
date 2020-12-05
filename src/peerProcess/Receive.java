package peerProcess;

import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/*
 *1. Receive msg
 *2. Release resource
 *3. Override run
 */

public class Receive implements Runnable{
	private String peerID;
	private Stats connectedPeer;
	private ArrayList<Stats> allConnectedPeers;
    private InputStream dis;
    private DataOutputStream dos;
    private Socket peer;
    private Message readMessage;
    private boolean isRunning = true;
    
    
    public Receive(Socket peer, String peerID,Stats connectedPeer, ArrayList<Stats> allConnectedPeers){
        this.peer = peer;
        this.peerID = peerID;
        this.connectedPeer = connectedPeer;
        this.allConnectedPeers = allConnectedPeers;
        try {
            dis = peer.getInputStream();
            dos = new DataOutputStream(peer.getOutputStream());
            connectedPeer.setDOS(dos);
            readMessage = new Message(dis);
            
        }catch (IOException e){
            System.out.println("====2====");
            this.release();
        }
    }

    //Release
    private void release() {
        this.isRunning = false;
        Utils.close(dis,peer);
    }

    @Override
    public void run() {
    	synchronized(dos)
    	{
    		SendMessage.sendHandshake(dos,peerID);
    		//also need to send bitfield here
    	}
    	String id = readMessage.readHandshake();

    	if(id.equals(connectedPeer.peerID))
    	{
        	System.out.println("Peer's ID correct: " + id);
    	}
    	else
    	{
    		System.out.println("Peer's ID incorrect: " + id);
    		this.release();
    	}
    	
    	// keep reading messages. 
    	// start a new thread to send corresponding message based on the message read.
    	// then update data rate etc. and check if need to send 'have' message.
        while (isRunning){
        	try {
        		readMessage.readNext();
        	}catch(IOException e) {
        		this.release();
        	}
        	
        	switch (readMessage.type)
        	{
        	case 0:
        		
        	case 1:
        	
        	case 2:
        	
        	case 3:
        	
        	case 4:
        	
        	case 5:
        		
        	case 6:
        		
        	case 7:
        	}
        }
    }
}
