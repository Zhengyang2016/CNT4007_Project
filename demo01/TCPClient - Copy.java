package demo01;

import java.io.FileInputStream;
import demo01.HandshakeMessage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class TCPClient {
    
	static String root = "C:/p2p/";
	static String baseLocation = root + "/ClientP1";
	static String chunksLocation = baseLocation + "/chunks";
	
	
	public static void main(String[] args) throws Exception{
    	
    	
        //读取E盘文件
		Socket socket = new Socket("127.0.0.1",8888);
		//Handshake
		HandshakeMessage.startHandshake(socket);
        FileInputStream fis = new FileInputStream("E:\\Ori.jpg");
        OutputStream os = socket.getOutputStream();
        
       
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fis.read(bytes)) != -1){
            os.write(bytes,0,len);
        }

        socket.shutdownOutput();

        InputStream is = socket.getInputStream();
        while ((len = is.read(bytes)) != -1){
            System.out.println(new String(bytes,0,len));
        }

        fis.close();
        socket.close();

    }
	
}   
