package com.hasten;

import com.hasten.sim.FileUtil;
import com.hasten.sim.SingleFileUploader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


class MockClientTest {

    public static final String BIG_FILE = "boltdb.pdf";

    public static final String SMALL_FILE = "text.txt";
    CountDownLatch countDownLatch = new CountDownLatch(2);

    @Test
    public void testMockClient() throws IOException, InterruptedException {
        SingleFileUploader uploader = new SingleFileUploader(BIG_FILE);
        uploader.uploadFile(false);

//        uploader.mergeChunk();
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