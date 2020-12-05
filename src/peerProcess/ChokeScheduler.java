package peerProcess;

import java.util.*;

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
		System.out.println("-Changing preferred neighbors...");
		ArrayList<Stats> interestedPeers = new ArrayList<Stats>();
		ArrayList<Stats> chokePeers;
		synchronized(connectedPeers) 
		{
			chokePeers = new ArrayList<Stats>(connectedPeers);
		}
		
		//get interested neighbors
		for (int i = 0; i < chokePeers.size(); ++i)
		{
			if(chokePeers.get(i).interested)
				interestedPeers.add(chokePeers.get(i));
		}
		
		//interested neighbors <= k, unchoke all the interested neighbors
		if (interestedPeers.size() <= k)
		{
			for(int i = 0; i < interestedPeers.size(); ++i)
			{	
				if(interestedPeers.get(i).choke)
				{
					new Thread( new Send(interestedPeers.get(i).out,1) ).start();
					interestedPeers.get(i).choke = false;
				}
				chokePeers.remove(interestedPeers.get(i));
				System.out.println("-Preferred neighbors: " + interestedPeers.get(i).peerID);
			}
			
		}
//!!!!!!need an eles if(have complete file) here to randomly choose preferred neighbors
		//else sort interested neighbors by speed in descending order, unchoke first k neighbors
		else
		{
			Collections.sort(interestedPeers, new Comparator<Stats>() {
	            @Override
	            public int compare(Stats p1, Stats p2) {
	                return Float.compare( p2.speed, p1.speed );
	            }
	        });
			for(int i = 0; i < k; ++i)
			{	
				if(interestedPeers.get(i).choke)
				{
					new Thread( new Send(interestedPeers.get(i).out,1) ).start();
					interestedPeers.get(i).choke = false;
				}
				chokePeers.remove(interestedPeers.get(i));
				System.out.println("-Preferred neighbors: " + interestedPeers.get(i).peerID);
			}
			
		}
		//choke the rest peers
		for(int i = 0; i < chokePeers.size(); ++i)
		{
			if( !(chokePeers.get(i).choke || chokePeers.get(i).optimisticUnchoke) )
			{
				new Thread( new Send(chokePeers.get(i).out,0) ).start();
				chokePeers.get(i).choke = true;
			}
		}
		
		
	}
}
