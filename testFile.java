/*
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class testFile {
    public class BitFieldHandler {
        /**
         * @param bitField The received byte[] for bitfield
         * @return int[] for bitfield
         * 0 for not have the piece, 1 for have
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
}
 */
/*
package peerProcess;

        import java.io.InputStream;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.net.Socket;
        import java.util.ArrayList;
        import java.util.concurrent.TimeUnit;

/*
 *1. Receive msg
 *2. Release resource
 *3. Override run


public class Receive implements Runnable{

    private Stats connectedPeer;
    private ArrayList<Stats> allConnectedPeers;
    private InputStream dis;
    private DataOutputStream dos;
    private Socket peer;
    private Message readMessage;
    private MyStats myStats;
    private long startTime = 0;
    private int lastRequest = -1;
    private boolean isRunning = true;
    private int pieceHave = 0;


    public Receive(Socket peer, MyStats myStats, Stats connectedPeer, ArrayList<Stats> allConnectedPeers){
        this.peer = peer;
        this.myStats = myStats;
        this.connectedPeer = connectedPeer;
        this.allConnectedPeers = allConnectedPeers;
        try {
            dis = peer.getInputStream();
            dos = new DataOutputStream(peer.getOutputStream());
            connectedPeer.setDOS(dos);
            readMessage = new Message(dis);

        }catch (IOException e){
            System.out.println("====2====");
            this.release();
        }
    }

    //Release
    private void release() {
        this.isRunning = false;
        Utils.close(dis,peer);
    }

    @Override
    public void run() {
        synchronized(dos)
        {
            SendMessage.sendHandshake(dos, myStats.peerID);
            SendMessage.sendBitfield(dos, myStats.bitfield);
        }
        synchronized(allConnectedPeers)
        {
            allConnectedPeers.add(connectedPeer);
        }

        String id = readMessage.readHandshake();

        if(id.equals(connectedPeer.peerID))
        {
            System.out.println("Peer's ID correct: " + id);
        }
        else
        {
            System.out.println("Peer's ID incorrect: " + id);
            this.release();
        }



    	/* keep reading messages.
    	start a new thread to send corresponding message based on the message read.
    	then update data rate etc. and check if need to send 'have' message.
        while (isRunning){
            try {
                readMessage.readNext();
            }catch(IOException e) {
                this.release();
            }

            switch (readMessage.type)
            {
                case 0://choke: set chokeMe to true. see if last request piece is received, if not, set that -complete
                    //piece to 0 in the bitfield													-complete
                    connectedPeer.chokeMe = true;
                    //choking log
                    Log.chokingLog(myStats.peerID, connectedPeer.peerID);
                    if(lastRequest >= 0)
                    {
                        BitFieldHandler.resetBit(myStats.bitfield, lastRequest);
                        lastRequest = -1;
                    }
                    break;

                case 1://unchoke: send request message or not interested if peer has no interesting piece  -complete
                    // upon receiving this unchoke; start timing download time; set chokeMe to false  -complete
                    connectedPeer.chokeMe = false;
                    //unchoking log
                    Log.unchokingLog(myStats.peerID, connectedPeer.peerID);
                    if( !myStats.downloadFinished && BitFieldHandler.interested(connectedPeer.bitfield, myStats.bitfield)  && (lastRequest < 0) )
                    {
                        int request = BitFieldHandler.selectPiece(connectedPeer.bitfield,myStats.bitfield );
                        if(request != -1)
                        {
                            synchronized(dos)
                            {
                                SendMessage.sendRequest(dos,request);
                            }
                            lastRequest = request;
                            startTime = System.nanoTime();
                        }
                    }
                    break;

                case 2://interested: set interested to true in stats	-complete
                    connectedPeer.interested = true;
                    //receiving 'interested' message log
                    Log.interestedLog(myStats.peerID, connectedPeer.peerID);
                    break;

                case 3://not interested: set interested to false in stats	-complete
                    connectedPeer.interested = false;
                    //receiving 'not interested' message log
                    Log.notInterestedLog(myStats.peerID, connectedPeer.peerID);
                    break;

                case 4://have: update bitfield and send interested/!interested		-complete

                    BitFieldHandler.setBit(connectedPeer.bitfield, readMessage.have);
                    //receiving 'have' message log
                    Log.haveLog(myStats.peerID, connectedPeer.peerID, readMessage.have);
                    if( BitFieldHandler.interested(connectedPeer.bitfield, myStats.bitfield) || lastRequest >= 0)
                    {
                        synchronized(dos)
                        {
                            SendMessage.sendInterested(dos);
                        }
                    }
                    else
                    {
                        synchronized(dos)
                        {
                            SendMessage.sendNotInterested(dos);
                        }
                    }

                    if(!connectedPeer.completeFile)
                        connectedPeer.completeFile = BitFieldHandler.downloadFinished( connectedPeer.bitfield, myStats.sparebits);

                    break;

                case 5://bitfield: store others' bitfield and compare with own 			-complete
                    //bitfield to send interested/not interested					-complete
                    connectedPeer.bitfield = readMessage.bitfield;
                    if( BitFieldHandler.interested(connectedPeer.bitfield, myStats.bitfield) )
                    {
                        synchronized(dos)
                        {
                            SendMessage.sendInterested(dos);
                        }
                    }
                    else
                    {
                        synchronized(dos)
                        {
                            SendMessage.sendNotInterested(dos);
                        }
                    }
                    connectedPeer.completeFile = BitFieldHandler.downloadFinished( connectedPeer.bitfield, myStats.sparebits);
                    break;

                case 6://request: check if peer is choked, if not, send the piece
                    byte[] piece;
                    if(!connectedPeer.choke)
                    {
                        //Verify if have the full file
                        if(myStats.haveFullFile){
                            String src = myStats.destDir + myStats.fileName;
                            piece = PieceManager.split(src, myStats.pieceSize, myStats.pieceNum, readMessage.request);
                        }else {
                            piece = PieceManager.toSend(myStats.destPaths, myStats.pieceSize, myStats.pieceNum,
                                    myStats.fileSize, readMessage.request);
                        }

                        new Thread( new Send(dos, readMessage.request, piece)).start();
                    }
                    break;

                case 7://piece: calculate and update speed.												-complete
                    //put into file.
                    //update own bitfield, send have to all other peers and decide whether to send 	     -complete
                    //not interested to other peers, then send next request to peer if peer does not choke me  -complete
                    lastRequest = -1;

                    long endTime = System.nanoTime();
                    long downloadTime = (endTime - startTime) / 1000000;//in millisecond
                    connectedPeer.speed = (readMessage.length - 1) / downloadTime;// byte/millisecond


                    PieceManager.toFile(readMessage.piece, myStats.destPaths, readMessage.pieceIndex);


                    if ( BitFieldHandler.downloadFinished(myStats.bitfield, myStats.sparebits) ){
                        myStats.downloadFinished = true;
                        Log.completionLog(myStats.peerID);
                        try {
                            String src = myStats.destDir + myStats.fileName;
                            PieceManager.merge(src, myStats.destPaths);
                        } catch (IOException e) {
                            System.out.println("Merge failed");
                            e.printStackTrace();
                        }
                    }

                    BitFieldHandler.setBit(myStats.bitfield, readMessage.pieceIndex);
                    //downloading a piece log
                    pieceHave ++;
                    Log.downloadingLog(myStats.peerID, connectedPeer.peerID, readMessage.pieceIndex, pieceHave);

                    // send have messages
                    for(int i = 0; i < allConnectedPeers.size();++i)
                    {
                        synchronized(allConnectedPeers.get(i).out)
                        {
                            SendMessage.sendHave(allConnectedPeers.get(i).out, readMessage.pieceIndex);
                        }
                    }
                    //send not interested messages
                    for(int i = 0; i < allConnectedPeers.size();++i)
                    {
                        if( !BitFieldHandler.interested(allConnectedPeers.get(i).bitfield, myStats.bitfield) )
                        {
                            synchronized(allConnectedPeers.get(i).out)
                            {
                                SendMessage.sendNotInterested(allConnectedPeers.get(i).out);
                            }
                        }
                    }
                    // send next request
                    if( !myStats.downloadFinished && !connectedPeer.chokeMe && BitFieldHandler.interested(connectedPeer.bitfield, myStats.bitfield) )
                    {
                        int request = BitFieldHandler.selectPiece(connectedPeer.bitfield,myStats.bitfield );
                        if(request != -1)
                        {
                            synchronized(dos)
                            {
                                SendMessage.sendRequest(dos,request);
                            }
                            lastRequest = request;
                            startTime = System.nanoTime();
                        }
                    }
                    break;
            }
        }
    }
}
*/