package peerProcess;

import java.io.DataOutputStream;

//states of connected peer
public class Stats {
	
	public String peerID;
	
	public volatile boolean interested = false;
	public volatile boolean choke = true;
	public volatile boolean optimisticUnchoke = false;
	public volatile boolean chokeMe = true;
	public volatile float speed = -1;
	public DataOutputStream out;
	public volatile int requesting = -1;
	
	public Stats(String ID){
		this.peerID = ID;
	}
	
	public void setDOS(DataOutputStream dos)
	{
		this.out = dos;
	}
	
}
