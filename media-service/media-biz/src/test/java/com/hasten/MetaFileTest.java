package com.hasten;


import com.hasten.sim.MetaFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.hasten.sim.Parser.*;

class MetaFileTest {

    public static final String BIG_FILE = "boltdb.pdf";

    public static final String SMALL_FILE = "text.txt";

    @Test
    public void testMeta() throws IOException {

        MetaFile metaFile = new MetaFile(SMALL_FILE);
        metaFile.readMetaFile();
    }

    @Test
    public void readAndWrite() throws IOException {
        File file = new File("testtttt.txt");

        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel channel = raf.getChannel();

        /*--------------------------------------------------------------*/
        final int OFFSET_1 = 0;
        final int OFFSET_2 = 4;

        channel.write(ByteBuffer.wrap(int2Byte(123)), OFFSET_1);
        channel.write(ByteBuffer.wrap(long2Byte(456L)), OFFSET_2);

        /*-------------------------------------*/
        byte[] intBuf = new byte[4];
        channel.position(OFFSET_1);
        channel.read(ByteBuffer.wrap(intBuf), OFFSET_1);
        int value = parseInt(intBuf);


        byte[] longBuf = new byte[8];
        channel.position(OFFSET_2);
        channel.read(ByteBuffer.wrap(longBuf), OFFSET_2);
        long value2 = parseLong(longBuf);


        System.out.println("int:" + value + "  long:" + value2);

    }


}