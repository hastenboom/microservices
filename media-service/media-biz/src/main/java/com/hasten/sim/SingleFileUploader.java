package com.hasten.sim;

import lombok.Data;
import lombok.val;
import lombok.var;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Hasten
 */
@Data
public class SingleFileUploader {

    private FileUtil sourceUtil;
    private FileUtil targetUtil;
    private DirUtil chunkDirUtil;
    private Executor threadPool;

    private MetaFile metaFile;

    private long chunkCount;

    //    public static final long CHUNK_SIZE = 1024 * 1024;//1MB
    public static final long CHUNK_SIZE = 30;

    private CountDownLatch countDownLatch;

    public SingleFileUploader(String filePath) throws IOException {
        this.sourceUtil = new FileUtil(filePath);
        this.targetUtil = new FileUtil("target-" + filePath);
        this.chunkDirUtil = new DirUtil(filePath + ".chunks");
        this.threadPool = Executors.newFixedThreadPool(3);
        this.chunkCount = sourceUtil.getChunkCount(CHUNK_SIZE);
        this.metaFile = new MetaFile(filePath);
    }

    public void uploadFile(boolean isAsync) throws InterruptedException, IOException {

        long begin = System.currentTimeMillis();

        if (!isAsync) {
            this.syncUpload();
        }
        else {
            countDownLatch = new CountDownLatch((int) chunkCount);
            this.asyncUpload(countDownLatch);
            countDownLatch.await();
        }
        long end = System.currentTimeMillis();
        System.out.println("Upload complete, time used: " + (end - begin) + "ms");
    }

    private void syncUpload() {

        List<Integer> notAckedBlkIdList = metaFile.getNotAckedBlkIdList();
        for (var i : notAckedBlkIdList) {
            try (FileUtil chunkFileUtil = new FileUtil(this.getChunkFilePath(i), "rw")) {

                int readSize = getReadSize(i);
                byte[] buf = sourceUtil.read((long) (i - 1) * CHUNK_SIZE, readSize);

                chunkFileUtil.write(0, buf);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

//        for (int i = 1; i <= chunkCount; i++) {
//        }

    }


    private void asyncUpload(CountDownLatch countDownLatch) throws IOException {
        for (int i = 1; i <= chunkCount; i++) {
            threadPool.execute(new UploadChunkTask(i, countDownLatch));
        }
    }

    class UploadChunkTask implements Runnable {
        int i;
        CountDownLatch countDownLatch;

        public UploadChunkTask(int chunkIndex, CountDownLatch countDownLatch) {
            this.i = chunkIndex;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try (FileUtil chunkFileUtil = new FileUtil(getChunkFilePath(i), "rw")) {
                int readSize = getReadSize(i);
                byte[] buf = sourceUtil.read((long) (i - 1) * CHUNK_SIZE, readSize);
                chunkFileUtil.write(0, buf);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                countDownLatch.countDown();
            }
        }
    }

    public void mergeChunk() throws IOException, InterruptedException {

        List<FileUtil> chunkRafList = chunkDirUtil.getRafList();
        for (int i = 1; i <= chunkRafList.size(); i++) {

            FileUtil chunkFileUtil = chunkRafList.get(i - 1);
            int readSize = getReadSize(i);

            byte[] read = chunkFileUtil.read(0, readSize);
            targetUtil.write((long) (i - 1) * CHUNK_SIZE, read);
        }
    }

    private String getChunkFilePath(int chunkIndex) {
        return chunkDirUtil.getChunkDirPath() + File.separator + chunkIndex + ".chunk";
    }

    private int getReadSize(int i) {
        int readSize;
        if (i == chunkCount) {
            readSize = (int) (sourceUtil.getLength() % CHUNK_SIZE);
        }
        else {
            readSize = (int) CHUNK_SIZE;
        }
        return readSize;
    }
}

