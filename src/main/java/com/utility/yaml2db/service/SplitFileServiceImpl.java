package com.utility.yaml2db.service;

import com.utility.yaml2db.constant.Constants;

import java.io.*;
import java.net.URL;

public class SplitFileServiceImpl {
    public void splitFiles(long splitInto) throws Exception {
       /* URL url = new URL(Constants.urls);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));
        RandomAccessFile ref = new RandomAccessFile(in.readLine(), "r");
*/
        RandomAccessFile raf = new RandomAccessFile(Constants.INPUT_SPLIT_FILE, "r");
        long numSplits = splitInto; //from user input, extract it from args
        long sourceSize = raf.length();
        long bytesPerSplit = sourceSize / numSplits;
        long remainingBytes = sourceSize % numSplits;

        int maxReadBufferSize = 8 * 1024; //8KB
        for (int destIx = 1; destIx <= numSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(Constants.SPLIT_FILE + destIx + ".yaml"));
            if (bytesPerSplit > maxReadBufferSize) {
                long numReads = bytesPerSplit / maxReadBufferSize;
                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                for (int i = 0; i < numReads; i++) {
                    readWrite(raf, bw, maxReadBufferSize);
                }
                if (numRemainingRead > 0) {
                    readWrite(raf, bw, numRemainingRead);
                }
            } else {
                readWrite(raf, bw, bytesPerSplit);
            }
            bw.close();
        }
        if (remainingBytes > 0) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("/Users/oswalr/Documents/yaml2db/input/split-" + (numSplits + 1) + ".yaml"));
            readWrite(raf, bw, remainingBytes);
            bw.close();
        }
        System.out.println("done");
        raf.close();
    }

    static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException, IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if (val != -1) {
            bw.write(buf);
        }
    }
}
