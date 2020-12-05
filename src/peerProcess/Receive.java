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
    private SendMessage sendMessage;
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
            sendMessage = new SendMessage(dos);
            
        }catch (IOException e){
            System.out.println("====2====");
            this.release();
        }
    }

    //Receive
/*    private String receive(){
        String msg="";
        try {
            msg=dis.readUTF();
            //System.out.println(msg);
        }catch (IOException e){
            System.out.println("====4====");
            release();
        }
        return msg;
    }
*/
    //Release
    private void release() {
        this.isRunning = false;
        Utils.close(dis,peer);
    }

    @Override
    public void run() {
    	
    	sendMessage.sendHandshake(dos,peerID);
    	String id = readMessage.readHandshake();

    	if(id.equals(connectedPeer.peerID))
    	{
        	System.out.println(peerID + " "+ id);
    	}
    	
    	new Thread(new Runnable() {
    		private DataOutputStream dos;
    		public Runnable init(DataOutputStream dos) {
    			this.dos = dos;
    			return this;
    		}
    		
    	    public void run() {
    	    	synchronized(dos)
    	    	{
    	    		sendMessage.sendChoke(dos);
    	    	}
    	    }
    	}.init(dos)).start();
    	
    	
    	// keep reading messages. 
    	// start a new thread to send corresponding message based on the message read.
    	// then update data rate etc. and check if need to send 'have' message.
        while (isRunning){
        	try {
        		readMessage.readNext();
        	}catch(IOException e) {
        		this.release();
        	}
        	
        	
        }
    }
}
