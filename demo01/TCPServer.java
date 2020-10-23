package demo01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServer {
    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(8888);

        //Keep running
        while (true){
            Socket socket = server.accept();

            //multithread
            new Thread(new Runnable() {
                //file upload
                @Override
                public void run() {
                    try {
                        InputStream is = socket.getInputStream();

                        File file = new File("F:\\upload");
                        if(!file.exists()){
                            file.mkdirs();
                        }

                        String fileName = "itcast"+System.currentTimeMillis()+new Random().nextInt(9999)+".jpg";

                        FileOutputStream fos = new FileOutputStream(file+"\\"+fileName);
                        int len = 0;
                        byte[] bytes = new byte[1024];
                        while ((len=is.read(bytes))!=-1){
                            fos.write(bytes,0,len);
                        }

                        socket.getOutputStream().write("上传成功".getBytes());

                        fos.close();
                        socket.close();
                    }catch (IOException e){
                        System.out.println(e);
                    }
                }
            }).start();


        }
    }
}
