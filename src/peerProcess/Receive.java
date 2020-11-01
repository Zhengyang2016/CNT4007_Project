package peerProcess;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/*
 *1. Receive msg
 *2. Release resource
 *3. Override run
 */

public class Receive implements Runnable{
    private DataInputStream dis;
    private Socket peer;
    private boolean isRunning = true;

    public Receive(Socket peer){
        this.peer = peer;
        try {
            dis = new DataInputStream(peer.getInputStream());
        }catch (IOException e){
            System.out.println("====2====");
            this.release();
        }
    }

    //Receive
    private String receive(){
        String msg="";
        try {
            msg=dis.readUTF();
            //System.out.println(msg);
        }catch (IOException e){
            System.out.println("====4====");
            release();
        }
        return msg;
    }

    //Release
    private void release() {
        this.isRunning = false;
        Utils.close(dis,peer);
    }

    @Override
    public void run() {
        while (isRunning){
            String msg = receive();
            if (!msg.equals("")){
                System.out.println(msg);
            }
        }
    }
}
