package peerProcess;

import java.io.*;
import java.util.List;
import java.util.Vector;

public class PieceManager {
    //src file
    private File src;
    //each piece's size
    private int pieceSize;
    //total piece count
    private int pieceNum;
    private int pieceIndex;

    public PieceManager(String srcPath, int pieceSize, int pieceNum, int pieceIndex){
        this.src = new File(srcPath);
        this.pieceSize = pieceSize;
        this.pieceNum = pieceNum;
        this.pieceIndex = pieceIndex;
    }

    /**
     * Split the file
     * @return byte[] for transmission
     */
    public byte[] split(){
        int actualSize;
        int lastPieceSize = (int)src.length() - (pieceNum-1)*pieceSize;
        int beginPos;
        byte[] piece = null;

        beginPos = pieceIndex*pieceSize;
        if (pieceIndex == pieceNum-1){//The last piece
            actualSize = lastPieceSize;
        }else {
            actualSize = pieceSize;
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(this.src,"r");
            raf.seek(beginPos);
            piece = new byte[actualSize];
            raf.read(piece);
            Utils.close(raf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return piece;
    }
    
    /**
     * write a piece into a byte[] and ready to send
     * @param destPaths a list of each piece's address
     * @param pieceSize each piece's size
     * @param pieceNum total piece count
     * @param fileSize full file size
     * @param pieceIndex pieceIndex
     * @return byte[] for transmission
     */
    public static byte[] toSend(List<String> destPaths, int pieceSize, int pieceNum, int fileSize, int pieceIndex){
        int actualSize;
        int lastPieceSize = fileSize - (pieceNum-1)*pieceSize;
        byte[] piece = null;

        if (pieceIndex == pieceNum-1){//The last piece
            actualSize = lastPieceSize;
        }else {
            actualSize = pieceSize;
        }

        try {
            InputStream is = new BufferedInputStream(new FileInputStream(destPaths.get(pieceIndex)));
            piece = new byte[actualSize];
            is.read(piece);
            Utils.close(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return piece;
    }

    /**
     * Write the byte[] received to a local file
     * @param piece the byte[] received
     * @param destPaths A list of each piece's address
     * @param pieceIndex pieceIndex
     */
    public static void toFile(byte[] piece,List<String> destPaths,int pieceIndex){
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(destPaths.get(pieceIndex)));
            os.write(piece);
            os.flush();
            os.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        //Bitfield.setBit(bitfield, pieceIndex);
        //sendHave(pieceIndex);
    }

    /**
     * Merge the pieces into a complete file
     * @param destPath Output address
     * @param destPaths A list of each piece's address
     */
    public static void merge(String destPath,List<String> destPaths) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(destPath,true));
        Vector<InputStream> vi = new Vector<InputStream>();
        SequenceInputStream sis = null;

        for (int i=0;i<destPaths.size();i++){
            vi.add(new BufferedInputStream(new FileInputStream(destPaths.get(i))));
        }
        sis = new SequenceInputStream(vi.elements());

        byte[] flush = new byte[1024];
        int len = -1;
        while ((len=sis.read(flush))!=-1){
            os.write(flush,0,len);
        }
        os.flush();
        sis.close();
        os.close();
    }

    public static void test() {
        PieceManager test = new PieceManager(
                "F:\\Download\\CNT4007_Project-MultiThreadConnection\\src\\peerProcess\\Message.java",
                1000,4,2);
        String str = new String(test.split(),0,test.split().length);
        System.out.println(str);
    }
}
