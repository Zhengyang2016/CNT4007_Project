package peerProcess;

import java.util.TimerTask;
import java.util.ArrayList;

public class OptimisticScheduler extends TimerTask{
	
	public ArrayList<Stats> connectedPeers;
	
	public OptimisticScheduler(int k, ArrayList<Stats> connectedPeers)
	{
		this.connectedPeers = connectedPeers;
	}
	
	public void run() 
	{
		
	}
}
