package com.hasten;

import com.hasten.sim.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;


class MockClientTest {

    public static final String BIG_FILE = "boltdb.pdf";

    public static final String SMALL_FILE = "text.txt";
    CountDownLatch countDownLatch = new CountDownLatch(2);

    public static final BlockingQueue<Block> blockQueue = new ArrayBlockingQueue<>(10);

    public static final BlockingQueue<Block> ackQueue = new ArrayBlockingQueue<>(10);

    @SneakyThrows
    @Test
    public void testMockClient() throws IOException, InterruptedException {

        new Thread(this::testServer).start();
        SingleFileUploader uploader = new SingleFileUploader(SMALL_FILE, blockQueue, ackQueue);

        uploader.getMetaFile().printMetaFile();
        uploader.uploadFile();
        uploader.getMetaFile().printMetaFile();
//        uploader.getBlockQueue().printQueueStatus();

//        uploader.mergeChunk();
    }

    public void testServer() {

        try {
            SingleFileAcceptor singleFileAcceptor = new SingleFileAcceptor(blockQueue, ackQueue);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testFileUtil() throws IOException, InterruptedException {
        new Thread(this::task).start();
        new Thread(this::task).start();
        countDownLatch.await();
    }

    public void task() {
        String str = "hello";
        try {
            new FileUtil(SMALL_FILE).write(110, str.getBytes());
            countDownLatch.countDown();
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}