package peerProcess;

import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/*
 *1. Receive msg
 *2. Release resource
 *3. Override run
 */

public class Receive implements Runnable{
	private String peerID;
    private InputStream dis;
    private DataOutputStream dos;
    private Socket peer;
    private Message readMessage;
    private SendMessage sendMessage;
    private boolean isRunning = true;
    
    
    public Receive(Socket peer, String peerID){
        this.peer = peer;
        try {
            dis = peer.getInputStream();
            dos = new DataOutputStream(peer.getOutputStream());
            readMessage = new Message(dis);
            sendMessage = new SendMessage(dos);
            this.peerID = peerID;
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
    	
    	sendMessage.sendHandshake(peerID);
    	String id = readMessage.readHandshake();
    	System.out.println(peerID + " "+ id);
    	
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
