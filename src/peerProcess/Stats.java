package peerProcess;

import java.io.DataOutputStream;

//states of connected peer
public class Stats {
	
	public String peerID;
	
	public boolean interested = false;
	public boolean choke = false;
	public float speed = -1;
	public DataOutputStream out;
	
	public Stats(String ID){
		this.peerID = ID;
	}
	
	public void setDOS(DataOutputStream dos)
	{
		this.out = dos;
	}
	
}
