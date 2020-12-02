package peerProcess;

import java.util.TimerTask;
import java.util.ArrayList;

public class ChokeScheduler extends TimerTask{
	
	public ArrayList<Stats> connectedPeers;
	public int k;
	
	public ChokeScheduler(int k, ArrayList<Stats> connectedPeers)
	{
		this.k = k;
		this.connectedPeers = connectedPeers;
	}
	
	public void run() 
	{
		
	}
}
