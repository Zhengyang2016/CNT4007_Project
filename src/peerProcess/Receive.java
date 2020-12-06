package peerProcess;

import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/*
 *1. Receive msg
 *2. Release resource
 *3. Override run
 */

public class Receive implements Runnable{

	private Stats connectedPeer;
	private ArrayList<Stats> allConnectedPeers;
    private InputStream dis;
    private DataOutputStream dos;
    private Socket peer;
    private Message readMessage;
    private MyStats myStats;
    private long startTime = 0;
    private int lastRequest = -1;
    private boolean isRunning = true;
    
    
    public Receive(Socket peer, MyStats myStats,Stats connectedPeer, ArrayList<Stats> allConnectedPeers){
        this.peer = peer;
        this.myStats = myStats;
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
    		SendMessage.sendHandshake(dos, myStats.peerID);
    		SendMessage.sendBitfield(dos, myStats.bitfield);
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
        	case 0://choke: set chokeMe to true. see if last request piece is received, if not, set that -complete
        			//piece to 0 in the bitfield													-complete
        		connectedPeer.chokeMe = true;
        		if(lastRequest >= 0)
        		{
        			BitFieldHandler.resetBit(myStats.bitfield, lastRequest);
        			lastRequest = -1;
        		}
        		break;
        		
        	case 1://unchoke: send request message or not interested if peer has no interesting piece  -complete
        			// upon receiving this unchoke; start timing download time; set chokeMe to false  -complete
        		connectedPeer.chokeMe = false;
        		if( !myStats.downloadFinished && BitFieldHandler.interested(connectedPeer.bitfield, myStats.bitfield)  && (lastRequest < 0) )
        		{
        			int request = BitFieldHandler.selectPiece(connectedPeer.bitfield,myStats.bitfield );
        			if(request != -1)
        			{
        				synchronized(dos)
            			{
            				SendMessage.sendRequest(dos,request);
            			}
        				lastRequest = request;
        				startTime = System.nanoTime();
        			}
        		}
        		break;
        	
        	case 2://interested: set interested to true in stats	-complete
        		connectedPeer.interested = true;
        		break;
        	
        	case 3://not interested: set interested to false in stats	-complete
        		connectedPeer.interested = false;
        		break;
        	
        	case 4://have: update bitfield and send interested/!interested		-complete
        		
        		BitFieldHandler.setBit(connectedPeer.bitfield, readMessage.have);
        		if( BitFieldHandler.interested(connectedPeer.bitfield, myStats.bitfield) || lastRequest >= 0)
        		{
        			synchronized(dos)
        			{
        				SendMessage.sendInterested(dos);
        			}
        		}
        		else
        		{
        			synchronized(dos)
        			{
        				SendMessage.sendNotInterested(dos);
        			}
        		}
        		
        		if(!connectedPeer.completeFile)
        			connectedPeer.completeFile = BitFieldHandler.downloadFinished( connectedPeer.bitfield, myStats.sparebits);
        		
        		break;
        	
        	case 5://bitfield: store others' bitfield and compare with own 			-complete
        			//bitfield to send interested/not interested					-complete
        		connectedPeer.bitfield = readMessage.bitfield;
        		if( BitFieldHandler.interested(connectedPeer.bitfield, myStats.bitfield) )
        		{
        			synchronized(dos)
        			{
        				SendMessage.sendInterested(dos);
        			}
        		}
        		else
        		{
        			synchronized(dos)
        			{
        				SendMessage.sendNotInterested(dos);
        			}
        		}
        		connectedPeer.completeFile = BitFieldHandler.downloadFinished( connectedPeer.bitfield, myStats.sparebits);
        		break;
        		
        	case 6://request: check if peer is choked, if not, send the piece
        		if(!connectedPeer.choke)
        		{
        			
        			
        			new Thread( new Send(dos, readMessage.request, ?? ) ).start();
        		}
        		break;
        		
        	case 7://piece: calculate and update speed.												-complete
        			//put into file.
        			//update own bitfield, send have to all other peers and decide whether to send 	     -complete
        			//not interested to other peers, then send next request to peer if peer does not choke me  -complete
        		lastRequest = -1;
        		
        		long endTime = System.nanoTime();
        		long downloadTime = (endTime - startTime) / 1000000;//in millisecond
        		connectedPeer.speed = (readMessage.length - 1) / downloadTime;// byte/millisecond
        		
        		
        		
        		
        		
        		if ( BitFieldHandler.downloadFinished(myStats.bitfield, myStats.sparebits) )
        			myStats.downloadFinished = true;
        		BitFieldHandler.setBit(myStats.bitfield, readMessage.pieceIndex);
        		// send have messages
        		for(int i = 0; i < allConnectedPeers.size();++i)
        		{
        			synchronized(allConnectedPeers.get(i).out)
    				{
    					SendMessage.sendHave(allConnectedPeers.get(i).out, readMessage.pieceIndex);
    				}
        		}
        		//send not interested messages
        		for(int i = 0; i < allConnectedPeers.size();++i)
        		{	
        			if( !BitFieldHandler.interested(allConnectedPeers.get(i).bitfield, myStats.bitfield) )
        			{
        				synchronized(allConnectedPeers.get(i).out)
        				{
        					SendMessage.sendNotInterested(allConnectedPeers.get(i).out);
        				}
        			}
        		}
        		// send next request
        		if( !myStats.downloadFinished && !connectedPeer.chokeMe && BitFieldHandler.interested(connectedPeer.bitfield, myStats.bitfield) )
        		{
        			int request = BitFieldHandler.selectPiece(connectedPeer.bitfield,myStats.bitfield );
        			if(request != -1)
        			{
        				synchronized(dos)
            			{
            				SendMessage.sendRequest(dos,request);
            			}
        				lastRequest = request;
        				startTime = System.nanoTime();
        			}
        		}
        		break;
        	}
        }
    }
}
