package peerProcess;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/*
*1. Get msg from console
*2. Send msg
*3. Release resource
*4. Override run
 */

public class Send implements Runnable{
    private BufferedReader console;
    private DataOutputStream dos;
    private Socket peer;
    private boolean isRunning = true;

    public Send(Socket peer){
        this.peer = peer;
        console = new BufferedReader(new InputStreamReader(System.in));
        try {
            dos = new DataOutputStream(peer.getOutputStream());
        }catch (IOException e){
            System.out.println("====1====");
            this.release();
        }
    }

    //Get input from console
    private String getFromConsole(){
        try {
            return console.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    //Send
    private void send(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        }catch (IOException e){
            System.out.println("====3====");
            this.release();
        }
    }

    //Release
    private void release() {
        this.isRunning = false;
        Utils.close(dos,peer);
    }

    @Override
    public void run() {
        while (isRunning){
            String msg = getFromConsole();
            if (!msg.equals("")){
                send(msg);
            }
        }
    }


}
