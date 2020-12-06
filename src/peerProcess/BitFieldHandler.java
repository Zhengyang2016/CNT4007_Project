package peerProcess;

import java.util.ArrayList;
import java.util.Random;

public class BitFieldHandler {
    /**
     * @param bitField The received byte[] for bitfield
     * @return int[] for bitfield
     * 0 for not have the piece, 1 for have
     */
    public static int[] toIntArray(byte[] bitField){
        int[] binary = new int[8*bitField.length];
        int counter = 0;

        for (int i=0;i<8;i++){
            String str = "";
            for(int j=8;j>Integer.toBinaryString(bitField[i]).length();j--){
                str = str + "0";
            }
            str = str + Integer.toBinaryString(bitField[i]);

            for (int k=0;k<8;k++){
                boolean flag = str.charAt(k)=='0';
                if (flag){
                    binary[counter] = 0;
                }else {
                    binary[counter] = 1;
                }
                counter++;
            }
        }

        return binary;
    }

    /**
     * @param others Other peer's bitfield in int[]
     * @param self Self's bitfield in int[]
     * @return Randomly selected index of piece to be requested
     * If there is nothing to request, return -1
     */
    public static int selectPiece(int[] others,int[] self){
        int[] a = new int[others.length];
        int counter = 0;
        int selectedPieceIndex = 0;
        Random r = new Random();
        for (int i=0;i<others.length;i++){
            boolean flag = (others[i]==1&&self[i]==0);
            if (flag){
                a[i]=1;
                counter++;
            }else {
                a[i]=0;
            }
        }

        if (counter==0){
            return -1;
        }

//        for (int i=0;i<others.length;i++){
//            if (i%8==0){
//                System.out.println();
//            }
//            System.out.print(a[i]);
//        }
//        System.out.println();
//        System.out.println(counter);

        int random = r.nextInt(counter);
//        System.out.println(random);

        for (int i=0;i<a.length;i++){
            if (a[i] == 1&&random!=0){
                random--;
            }else if (a[i] == 1&&random==0){
                selectedPieceIndex = i;
                break;
            }
        }

        System.out.println("Selected PieceIndex is: " + selectedPieceIndex);

        return selectedPieceIndex;
    }

    /**
     * Checks if the selected piece has been requested
     * @param selectedPiece selected piece index
     * @param connectedPeers ArrayList that contain all connected peers
     * @return ture for not requested yet; false for already requested
     */
    public static boolean alreadyRequested(int selectedPiece, ArrayList<Stats> connectedPeers){
        for (int i=0; i<connectedPeers.size(); i++){
            if (selectedPiece == connectedPeers.get(i).requesting){
                return false;
            }
        }
        return true;
    }

    public static void test(){
        byte[] test = new byte[8];
        for (int i=0;i<8;i++){
            test[i] = (byte)(i*13);
        }

        for (int i=0;i<8;i++){
            for (int j=8;j>Integer.toBinaryString(test[i]).length();j--){
                System.out.print(0);
            }
            System.out.println(Integer.toBinaryString(test[i]));
        }

        System.out.println("\n");

        for (int i=0;i<8;i++){
            for (int k=0;k<8;k++){
                int a = i*8+k;
                System.out.print(toIntArray(test)[a]);
            }
            System.out.print("\n");
        }

        int[] a = {0,0,0,0,0,1,1,1,1,0,1,0,0,1,1,1,1,1,1,0,0,1,1,1,1,0,1,0,0,1,1,1};
        int[] b = {0,1,0,0,0,0,1,1,1,0,0,0,0,1,1,1,1,0,0,1,0,1,1,1,0,0,0,0,0,1,1,1};

        selectPiece(a,b);
    }
}
