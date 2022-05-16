package com.utility.yaml2db.service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SplitFileServiceImpl {
    public static void main(String[] args) throws Exception
    {
        RandomAccessFile raf = new RandomAccessFile("/Users/oswalr/Documents/yaml2db/input/202203252200-bulk-way-sectioned.2022.03.210.WLD.lookup.txt", "r");
        long numSplits = 20; //from user input, extract it from args
        long sourceSize = raf.length();
        long bytesPerSplit = sourceSize/numSplits ;
        long remainingBytes = sourceSize % numSplits;

        int maxReadBufferSize = 8 * 1024; //8KB
        for(int destIx=1; destIx <= numSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("/Users/oswalr/Documents/yaml2db/input/split-"+destIx+".yaml"));
            if(bytesPerSplit > maxReadBufferSize) {
                long numReads = bytesPerSplit/maxReadBufferSize;
                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                for(int i=0; i<numReads; i++) {
                    readWrite(raf, bw, maxReadBufferSize);
                }
                if(numRemainingRead > 0) {
                    readWrite(raf, bw, numRemainingRead);
                }
            }else {
                readWrite(raf, bw, bytesPerSplit);
            }
            bw.close();
        }
        if(remainingBytes > 0) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("/Users/oswalr/Documents/yaml2db/input/split-"+(numSplits+1)+".yaml"));
            readWrite(raf, bw, remainingBytes);
            bw.close();
        }
        System.out.println("done");
        raf.close();
    }

    static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException, IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if(val != -1) {
            bw.write(buf);
        }
    }
}
