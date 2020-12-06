package peerProcess;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
//    public static void main(String[] args){
//        String[] test={"1006","1007","1023","1045"};
//        preferredNeighborsLog("1001",test);
//    }

    public static void log(String msg, String peerID){
        //Create log
        File src = new File("log_peer_"+peerID+".log");
        try {
            src.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Select stream
        BufferedWriter os = null;
        try {
            os = new BufferedWriter(new FileWriter(src,true));
            //String msg = getTime()+" : Peer [peer_ID 1] makes a connection to Peer [peer_ID 2].";
            //Write
            os.append("At "+getTime()+msg);
            os.newLine();
            os.flush();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            //Close
            if (null!=os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("HH:mm:ss:SSS");
        Date date = new Date();
        String time = sdf.format(date);
        return time;
    }

    public static void connectionLog(String peerID1, String peerID2){
        log(" : Peer "+peerID1+" makes a connection to Peer "+peerID2+".",peerID1);
    }
    public static void connectedLog(String peerID1, String peerID2){
        log(" : Peer "+peerID1+" is connected from Peer "+peerID2+".",peerID1);
    }
    public static void preferredNeighborsLog(String peerID1, String[] list){
        String str = "";
        for (int i=0;i<list.length;i++){
            if (i==list.length-1){
                str = str+list[i];
            }else {
                str = str+list[i]+", ";
            }
        }
        log(" : Peer "+peerID1+" has the preferred neighbors "+str+".",peerID1);
    }
    public static void unchokedNeighborLog(String peerID1, String peerID2){
        log(" : Peer "+peerID1+" has the optimistically unchoked neighbor "+peerID2+".",peerID1);
    }
    public static void unchokingLog(String peerID1, String peerID2){
        log(" : Peer "+peerID1+" is unchoked by "+peerID2+".",peerID1);
    }
    public static void chokingLog(String peerID1, String peerID2){
        log(" : Peer "+peerID1+" is choked by "+peerID2+".",peerID1);
    }
    public static void haveLog(String peerID1, String peerID2, int pieceIndex){
        log(" : Peer "+peerID1+" received the ‘have’ message from "+peerID2+" for the piece "+pieceIndex+".",peerID1);
    }
    public static void interestedLog(String peerID1, String peerID2){
        log(" : Peer "+peerID1+" received the ‘interested’ message from "+peerID2+".",peerID1);
    }
    public static void notInterestedLog(String peerID1, String peerID2){
        log(" : Peer "+peerID1+" received the ‘not interested’ message from "+peerID2+".",peerID1);
    }
    public static void downloadingLog(String peerID1, String peerID2, int pieceIndex, int pieceNum){
        log(" : Peer "+peerID1+" has downloaded the piece "+pieceIndex+" from "+peerID2+". Now the number of pieces it has is "+pieceNum+".",peerID1);
    }
    public static void completionLog(String peerID1){
        log(" : Peer "+peerID1+" has downloaded the complete file.",peerID1);
    }

}

