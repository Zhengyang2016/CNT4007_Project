package demo09_oopPeer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Peer1 {
    public static void main(String[] args) throws IOException {
        int peerID = 1;
        ServerSocket serverSocket = new ServerSocket(6666);

        while (true) {
            Socket server = serverSocket.accept();
            System.out.println("Get one connection");

            //new Thread(new Peer(server)).start();
            new Thread(new Send(server)).start();
            new Thread(new Receive(server)).start();
        }
    }


    //As server, unused
    static class Peer implements Runnable{
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket peer;
        private BufferedReader console;
        private boolean isRunning;

        public Peer(Socket peer){
            this.peer=peer;
            try {
                dis = new DataInputStream(peer.getInputStream());
                dos = new DataOutputStream(peer.getOutputStream());
                isRunning = true;
            } catch (IOException e) {
                System.out.println("----1----");
                release();
            }
            console = new BufferedReader(new InputStreamReader(System.in));
        }

        //Receive
        private String receive(){
            String msg="";
            try {
                msg=dis.readUTF();
                //System.out.println(msg);
            }catch (IOException e){
                System.out.println("----2----");
                release();
            }
            return msg;
        }
        //Send
        private void send(String msg){
            try {
                msg = console.readLine();
                dos.writeUTF(msg);
                dos.flush();
            }catch (IOException e){
                System.out.println("----3----");
                release();
            }
        }
        //Release
        private void release(){
            this.isRunning = false;
            Utils.close(dos,dis,peer);
        }

        @Override
        public void run() {
            while (isRunning){
                String msg = receive();
                System.out.println(msg);
                if (!msg.equals("")){
                    send(msg);
                }

            }
        }
    }
}
