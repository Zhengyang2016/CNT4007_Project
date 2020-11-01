package peerProcess;

//Release resource
import java.io.Closeable;

public class Utils {
    //release
    public static void close(Closeable... targets) {
        for (Closeable target:targets){
            try {
                if (null!=target){
                    target.close();
                }
            }catch (Exception e){

            }
        }

    }
}
