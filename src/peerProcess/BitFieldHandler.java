package peerProcess;

import java.util.ArrayList;
import java.util.Random;

public class BitFieldHandler {
    /**
     * @param bitField The received byte[] for bitfield
     * @return int[] for bitfield
     * 0 for not have the piece, 1 for have
     */
    public static int[] toIntArray(byte[] bitField){
        int[] binary = new int[8*bitField.length];
        int counter = 0;

        for (int i=0;i<8;i++){
            String str = "";
            for(int j=8;j>Integer.toBinaryString(bitField[i]).length();j--){
                str = str + "0";
            }
            str = str + Integer.toBinaryString(bitField[i]);

            for (int k=0;k<8;k++){
                boolean flag = str.charAt(k)=='0';
                if (flag){
                    binary[counter] = 0;
                }else {
                    binary[counter] = 1;
                }
                counter++;
            }
        }

        return binary;
    }

    /**
     * @param others Other peer's bitfield in int[]
     * @param self Self's bitfield in int[]
     * @return Randomly selected index of piece to be requested
     * If there is nothing to request, return -1
     */
    public static synchronized int selectPiece(byte[] peer,byte[] self){
    	byte[] request = new byte[self.length];
    	int n = 0;
    	Random rand = new Random();
    	
    	for(int i = 0;i < self.length;++i)
    	{
    		request[i] = (byte)(( self[i] | peer[i] ) ^ self[i] );
    		if (request[i] != 0)
    			n += 1;
    	}
    	if(n == 0)
    		return -1;
    	int selectByte = rand.nextInt(n);
    	
    	for(int i = 0;i < request.length;++i)
    	{
    		if(request[i] != 0)
    			n -= 1;
    		if (n == selectByte)
    		{	
    			int count = 0;
    			for(int j = 0;j<8;++j)
    			{
    				if ( ((request[i] >>> j) & 1 )== 1 )
    					count++;
    				//System.out.println(request[0]>>>j);
    			}
    			//System.out.println(count);
    			//System.out.println(request[0]);
    			
    			int selectPiece = rand.nextInt(count);
    			for(int j = 0;j<8;++j)
    			{
    				if ( ((request[i] << j) & 128 )== 128 )
    				{
    					count -= 1;
    				}
    				if ( count == selectPiece )
    				{
    					//System.out.println(i+" "+j);
    					setBit(self, (i*8 +j));
    					return (i*8 + j);
    				}
    			}
    		}
    		
    	}
    	
    	return -1;
    	
/*    	int[] others = toIntArray(peer);
    	int[] self = toIntArray(myself);
    	
        int[] a = new int[others.length];
        int counter = 0;
        int selectedPieceIndex = 0;
        Random r = new Random();
        for (int i=0;i<others.length;i++){
            boolean flag = (others[i]==1&&self[i]==0);
            if (flag){
                a[i]=1;
                counter++;
            }else {
                a[i]=0;
            }
        }

        if (counter==0){
            return -1;
        }

//        for (int i=0;i<others.length;i++){
//            if (i%8==0){
//                System.out.println();
//            }
//            System.out.print(a[i]);
//        }
//        System.out.println();
//        System.out.println(counter);

        int random = r.nextInt(counter);
//        System.out.println(random);

        for (int i=0;i<a.length;i++){
            if (a[i] == 1&&random!=0){
                random--;
            }else if (a[i] == 1&&random==0){
                selectedPieceIndex = i;
                break;
            }
        }

        System.out.println("Selected PieceIndex is: " + selectedPieceIndex);

        return selectedPieceIndex;
*/
    }
    
    public static void setBit(byte[] bytes, int index) {
		bytes[index/8] |= (128 >>> (index % 8));
	}
    
    public static synchronized void resetBit(byte[] bytes, int index) {
		bytes[index/8] &= (~(128 >>> (index % 8)));
	}
    
    public static synchronized boolean interested(byte[] peer, byte[] self) {
    	boolean interested = false;
    	
    	for(int i = 0; i < self.length; ++i)
    	{
    		if(peer[i] == self[i])
    			continue;
    		else
    		{
    			int diff = peer[i] ^ self[i];
    			if( (diff | self[i]) != self[i] )
    			{
    				interested = true;
    				break;
    			}
    		}
    	}
    	
    	return interested;
    }
    
    public static synchronized boolean downloadFinished(byte[] self, byte sparebits) {
    	
    	for(int i = 0;i<self.length-1;++i)
    	{
    		if(self[i] != -1)
    			return false;
    	}
    	if( self[self.length-1] != sparebits )
    		return false;
    	return true;
    }
    
    /**
     * Checks if the selected piece has been requested
     * @param selectedPiece selected piece index
     * @param connectedPeers ArrayList that contain all connected peers
     * @return ture for not requested yet; false for already requested
     */
/*    public static boolean alreadyRequested(int selectedPiece, ArrayList<Stats> connectedPeers){
        for (int i=0; i<connectedPeers.size(); i++){
            if (selectedPiece == connectedPeers.get(i).requesting){
                return false;
            }
        }
        return true;
    }
*/
    public static void test(){
 /*       byte[] test = new byte[8];
        for (int i=0;i<8;i++){
            test[i] = (byte)(i*13);
        }

        for (int i=0;i<8;i++){
            for (int j=8;j>Integer.toBinaryString(test[i]).length();j--){
                System.out.print(0);
            }
            System.out.println(Integer.toBinaryString(test[i]));
        }

        System.out.println("\n");

        for (int i=0;i<8;i++){
            for (int k=0;k<8;k++){
                int a = i*8+k;
                System.out.print(toIntArray(test)[a]);
            }
            System.out.print("\n");
        }
*/
        byte[] a = {0,-1,-1,0};//,0,1,1,1,1,0,1,0,0,1,1,1,1,1,1,0,0,1,1,1,1,0,1,0,0,1,1,1};
        byte[] b = {-1,-1,-1,-2};//,0,0,1,1,1,0,0,0,0,1,1,1,1,0,0,1,0,1,1,1,0,0,0,0,0,1,1,1};

        System.out.println(selectPiece(b,a));
        setBit(a,0);
        System.out.println("set: "+a[0] + " "+ a[1]+" "+a[2]+" "+a[3]);
        resetBit(a,0);
        System.out.println("reset: "+a[0] + " "+ a[1]+" "+a[2]+" "+a[3]);
        System.out.println("Intereested: "+ interested(b,a) );
        System.out.println("Downloaded: "+ downloadFinished(b,(byte)-4) );
    }
}
