import peerProcess.Config;
import java.util.ArrayList;
import java.net.*; 
import java.io.*;

public class peerProcess {
	public static void main(String[] args) {
		
		int NumberOfPreferredNeighbors;
		int UnchokingInterval;
		int OptimisticUnchokingInterval;
		String FileName;
		int FileSize;
		int PieceSize;
		String peerID = args[0];
		int peerIndex;//the position of this peer in the configuration file
		
		ArrayList<String[]> peerInfo = new ArrayList<String[]>();
		
		//reading configuration file
		Config cfg = new Config();//pass command line input to read config.
		NumberOfPreferredNeighbors = cfg.NumberOfPreferredNeighbors;
		System.out.println(NumberOfPreferredNeighbors);
		UnchokingInterval = cfg.UnchokingInterval;
		System.out.println(UnchokingInterval);
		OptimisticUnchokingInterval = cfg.OptimisticUnchokingInterval;
		System.out.println(OptimisticUnchokingInterval);
		FileName = cfg.FileName;
		System.out.println(FileName);
		FileSize = cfg.FileSize;
		System.out.println(FileSize);
		PieceSize = cfg.PieceSize;
		System.out.println(PieceSize);
		
		peerInfo = cfg.readPeerInfo(peerID);
		System.out.println(peerInfo.get(3)[0]);
		peerIndex = cfg.getIndex();
		System.out.println(peerIndex);
		
		
		
		
		//test functions of sending files
		ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException ex) {
            System.out.println("Can't setup server on this port number. ");
        }

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        //Create a client thread to send file
        Client client = new Client();
        client.start();
        
        try {
            socket = serverSocket.accept();
        } catch (IOException ex) {
            System.out.println("Can't accept client connection. ");
        }
        
        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            System.out.println("Can't get socket input stream. ");
        }

        try {
            out = new FileOutputStream("src/test2.png");
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        }

        byte[] bytes = new byte[16*1024];
        try {
        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }
        }catch(IOException e) {
        	System.out.println("IOException.");
        }
        try {
        out.close();
        in.close();
        socket.close();
        serverSocket.close();
        }catch(IOException e) {
        	System.out.println("close failed.");
        }
		
		
        
        
	}
}


class Client extends Thread 
{ 
    public void run() 
    { 
        try
        { 
        	 Socket socket = null;
             String host = "127.0.0.1";

             socket = new Socket(host, 4444);
             
             File file = new File("src/test.png");
             // Get the size of the file
             long length = file.length();
             byte[] bytes = new byte[16 * 1024];
             InputStream in = new FileInputStream(file);
             OutputStream out = socket.getOutputStream();
             
             int count;
             while ((count = in.read(bytes)) > 0) {
                 out.write(bytes, 0, count);
             }

             out.close();
             in.close();
             socket.close();
  
        } 
        catch (Exception e) 
        { 
            // Throwing an exception 
            System.out.println ("Exception is caught"); 
        } 
    } 
} 
