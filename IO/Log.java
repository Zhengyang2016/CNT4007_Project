package io03;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    /*
    public static void main(String[] args){
        connectionLog();
        connectedLog();
        preferredNeighborsLog();
        unchokedNeighborLog();
        unchokingLog();
        chokingLog();
        haveLog();
        interestedLog();
        notInterestedLog();
        downloadingLog();
        completionLog();
    }
    */

    public static void log(String msg){
        //Create log
        File src = new File("log_peer_[peerID].log");
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

    public static void connectionLog(){
        log(" : Peer [peer_ID 1] makes a connection to Peer [peer_ID 2].");
    }
    public static void connectedLog(){
        log(" : Peer [peer_ID 1] is connected from Peer [peer_ID 2].");
    }
    public static void preferredNeighborsLog(){
        log(" : Peer [peer_ID] has the preferred neighbors [preferred neighbor ID list].");
    }
    public static void unchokedNeighborLog(){
        log(" : Peer [peer_ID] has the optimistically unchoked neighbor [optimistically unchoked neighbor ID].");
    }
    public static void unchokingLog(){
        log(" : Peer [peer_ID 1] is unchoked by [peer_ID 2].");
    }
    public static void chokingLog(){
        log(" : Peer [peer_ID 1] is choked by [peer_ID 2].");
    }
    public static void haveLog(){
        log(" : Peer [peer_ID 1] received the ‘have’ message from [peer_ID 2] for the piece [piece index].");
    }
    public static void interestedLog(){
        log(" : Peer [peer_ID 1] received the ‘interested’ message from [peer_ID 2].");
    }
    public static void notInterestedLog(){
        log(" : Peer [peer_ID 1] received the ‘not interested’ message from [peer_ID 2].");
    }
    public static void downloadingLog(){
        log(" : Peer [peer_ID 1] has downloaded the piece [piece index] from [peer_ID 2]. Now the number of pieces it has is [number of pieces].");
    }
    public static void completionLog(){
        log(" : Peer [peer_ID] has downloaded the complete file.");
    }

}

