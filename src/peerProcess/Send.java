package peerProcess;

import java.io.DataOutputStream;

public class Send implements Runnable{
	private DataOutputStream dos;
	private int pieceIndex;
	private byte[] bitfield;
	private byte[] piece;
	private int messageType; //
	
	public Send(DataOutputStream dos, int type)
	{
		this.dos = dos;
		this.messageType = type;
	}
	
	public Send(DataOutputStream dos, int type, int pieceIndex)
	{
		this.dos = dos;
		this.messageType = type;
		this.pieceIndex = pieceIndex;
	}
	
	public Send(DataOutputStream dos, byte[] bitfield)
	{
		this.dos = dos;
		this.messageType = 5;
		this.bitfield = bitfield;
	}
	
	public Send(DataOutputStream dos,int pieceIndex, byte[] piece)
	{
		this.dos = dos;
		this.messageType = 7;
		this.piece = piece;
	}
	
    @Override
    public void run() {
        switch (messageType)
        {
        case 0:
        	synchronized(dos)
        	{
        		SendMessage.sendChoke(dos);
        	}
        	break;
        
        case 1:
        
        	synchronized(dos)
        	{
        		SendMessage.sendUnchoke(dos);
        	}
        	break;
        
        case 2:
        
        	synchronized(dos)
        	{
        		SendMessage.sendInterested(dos);
        	}
        	break;
        
        case 3:
        
        	synchronized(dos)
        	{
        		SendMessage.sendNotInterested(dos);
        	}
        	break;
        
        case 4:
        
        	synchronized(dos)
        	{
        		SendMessage.sendHave(dos,pieceIndex);
        	}
        	break;
        
        case 5:
        
        	synchronized(dos)
        	{
        		SendMessage.sendBitfield(dos,bitfield);
        	}
        	break;
        
        case 6:
        
        	synchronized(dos)
        	{
        		SendMessage.sendRequest(dos,pieceIndex);
        	}
        	break;
        
        case 7:
        
        	synchronized(dos)
        	{
        		SendMessage.sendPiece(dos,pieceIndex,piece);
        	}
        	break;
        }
    }


}
