package io03;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 封装分割
 */

public class SplitFile {
    //源头
    private File src;
    //目的地（文件夹）
    private String destDir;
    //分割文件储存路径
    private List<String> destPaths;
    //每块大小
    private int blockSize;
    //块数
    private int size;

    public SplitFile(String srcPath, String destDir, int blockSize){
        this.src = new File(srcPath);
        this.destDir = destDir;
        this.blockSize = blockSize;
        this.destPaths = new ArrayList<String>();

        //初始化
        init();
    }
    //初始化
    private void init(){
        //总长度
        long len = this.src.length();
        //块数
        this.size = (int) Math.ceil(len * 1.0 / blockSize);
        //路径
        for (int i=0;i<size;i++){
            this.destPaths.add(this.destDir+i+"-"+this.src.getName());
        }
    }

    /**
     * 分割
     * 1、计算每块起始位置 大小
     * 2、分割
     */
    public void split() throws IOException {
        //总长度
        long len = src.length();

        //实际大小
        int actualSize = (int) (blockSize > len ? len : blockSize);
        //起始位置
        int beginPos = 2;

        for (int i = 0; i < size; i++) {
            beginPos = i * blockSize;
            if (i == size - 1) {//最后一块
                actualSize = (int) len;
            } else {
                actualSize = blockSize;
                len -= actualSize;//剩余量
            }
            splitDetail(i, beginPos, actualSize);
        }

    }

    /**
     * 指定第i块的起始位置 实际长度
     * @param i
     * @param beginPos
     * @param actualSize
     * @throws IOException
     */

    private void splitDetail(int i, int beginPos, int actualSize) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.src,"r");
        RandomAccessFile raf2 = new RandomAccessFile(this.destPaths.get(i),"rw");
        //随机读取
        randomAccessFile.seek(beginPos);
        //读取
        byte[] flush = new byte[1024];//缓冲容器
        int len = -1;//接收长度
        while ((len = randomAccessFile.read(flush)) != -1) {

            if (actualSize > len) {//获取所有内容
                raf2.write(flush,0,len);
                actualSize -= len;
            } else {
                raf2.write(flush,0,actualSize);
                break;
            }
        }
        raf2.close();
        randomAccessFile.close();
    }

    /**
     * 文件的合并
     */
    public void merge(String destPath) throws IOException {
        //输出流
        OutputStream os = new BufferedOutputStream(new FileOutputStream(destPath,true));
        Vector<InputStream> vi = new Vector<InputStream>();
        SequenceInputStream sis = null;

        //输入流
        for (int i=0;i<destPaths.size();i++){
            vi.add(new BufferedInputStream(new FileInputStream(destPaths.get(i))));
        }
        sis = new SequenceInputStream(vi.elements());
        //拷贝
        byte[] flush = new byte[1024];//缓冲容器
        int len = -1;//接收长度
        while ((len=sis.read(flush))!=-1){
            os.write(flush,0,len);//分段写出
        }
        os.flush();
        sis.close();
        os.close();
    }

    public static void main(String[] args) throws IOException {
        SplitFile sf = new SplitFile("ori.jpg","E:/test/",1024*100);
        sf.split();
        sf.merge("E:/test/copy.jpg");
    }
}


