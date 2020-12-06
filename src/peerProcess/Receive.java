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
//    !!!	synchronized send bitfield here
    	}
    	synchronized(allConnectedPeers)
		{
			allConnectedPeers.add(connectedPeer);
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
    	
    	
    	
    	/* keep reading messages. 
    	start a new thread to send corresponding message based on the message read.
    	then update data rate etc. and check if need to send 'have' message. */
        while (isRunning){
        	try {
        		readMessage.readNext();
        	}catch(IOException e) {
        		this.release();
        	}
        	
        	switch (readMessage.type)
        	{
        	case 0://choke: set chokeMe to true. see if last request piece is received, if not, set that
        			//piece to 0 in the bitfield
        		
        		break;
        		
        	case 1://unchoke: send request message or not interested if peer has no interesting piece
        			// upon receiving this unchoke; set chokeMe to false
        		
        		break;
        	
        	case 2://interested: set interested to true in stats	-complete
        		connectedPeer.interested = true;
        		break;
        	
        	case 3://not interested: set interested to false in stats	-complete
        		connectedPeer.interested = false;
        		break;
        	
        	case 4://have: update bitfield and send interested/!interested
        		
        		break;
        	
        	case 5://bitfield: store others' bitfield and compare with own bitfield to send interested/not interested
        		
        		break;
        		
        	case 6://request: check if peer is choked, if not, send the piece
        		
        		break;
        		
        	case 7://piece: update own bitfield, send have to all other peers and decide whether to send 
        			//not interested to other peers, then send next request to peer if peer does not choke me
        		
        		break;
        	}
        }
    }
}
