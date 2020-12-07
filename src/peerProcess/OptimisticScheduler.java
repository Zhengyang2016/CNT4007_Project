package peerProcess;

import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Random;

public class OptimisticScheduler extends TimerTask{
	
	public ArrayList<Stats> connectedPeers;
	public Stats currentOptimisticPeer;
	public MyStats myStats;
	
	public OptimisticScheduler(ArrayList<Stats> connectedPeers, MyStats myStats)
	{
		this.connectedPeers = connectedPeers;
		this.myStats = myStats;
	}
	
	public void run() 
	{
		System.out.println("-Changing optimistically unchoked neighbor...");
		ArrayList<Stats> interestedPeers = new ArrayList<Stats>();
		for(int i = 0;i < connectedPeers.size(); ++i)
		{
			if(connectedPeers.get(i).interested && connectedPeers.get(i).choke)
				interestedPeers.add(connectedPeers.get(i));
		}
		
		if(currentOptimisticPeer != null)
		{
			currentOptimisticPeer.optimisticUnchoke = false;
		}
		
		if(interestedPeers.size() > 0)
		{
			Random rand = new Random();
			currentOptimisticPeer = interestedPeers.get( rand.nextInt( interestedPeers.size()) );
			
			new Thread( new Send(currentOptimisticPeer.out,1) ).start();
			currentOptimisticPeer.choke = false;
			currentOptimisticPeer.optimisticUnchoke = true;
			System.out.println("-Current optimistically unchoked neighbor: " + currentOptimisticPeer.peerID);
			//change of optimistically unchoked neighbor
			Log.unchokedNeighborLog(myStats.peerID,currentOptimisticPeer.peerID);
		}
		
		
	}
}
