package com.hasten.sim;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
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

    private Executor divideChunkPool;
    private Executor uploadChunkPool;


    private MetaFile metaFile;

    private long chunkCount;

    private final BlockingQueue<Block> blockQueue;

    private final BlockingQueue<Block> ackQueue;


    //    public static final long CHUNK_SIZE = 1024 * 1024;//1MB
    public static final long CHUNK_SIZE = 30;

    //late init
    private CountDownLatch countDownLatch;

    public SingleFileUploader(String filePath, BlockingQueue<Block> blockQueue, BlockingQueue<Block> ackQueue) throws IOException {
        this.sourceUtil = new FileUtil(filePath);
        this.targetUtil = new FileUtil("target-" + filePath);
        this.chunkDirUtil = new DirUtil(filePath + ".chunks");

        this.divideChunkPool = Executors.newFixedThreadPool(3);
        this.uploadChunkPool = Executors.newFixedThreadPool(3);

        this.chunkCount = sourceUtil.getChunkCount(CHUNK_SIZE);
        this.metaFile = new MetaFile(filePath);


        this.blockQueue = blockQueue;
        this.ackQueue = ackQueue;
    }

    public void uploadFile() throws InterruptedException, IOException {

        long begin = System.currentTimeMillis();

        new Thread(this::acceptAckTask).start();

        List<Integer> notSendAndSendingIdList = metaFile.getNotSendAndSendingIdList();
        List<Integer> notChuckIdList = metaFile.getNotChuckIdList();

        countDownLatch = new CountDownLatch(notChuckIdList.size() + notSendAndSendingIdList.size());
        //upload
        for (Integer i : notSendAndSendingIdList) {
            uploadChunkPool.execute(new UploadChunkTask(i));
        }

        //divide chunks
        for (var i : notChuckIdList) {
            divideChunkPool.execute(new DivideChunkTask(i));
        }

        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("Upload complete, time used: " + (end - begin) + "ms");
    }


    private void acceptAckTask() {
        while (true) {
            Block blk = null;
            try {
                blk = ackQueue.take();
                /*more status should be considered, but handle only the ack here*/
                metaFile.setChunkStatus(blk.getBlockId(), MetaFile.ACKED);
                countDownLatch.countDown();
                if (metaFile.uploadIsDone()) {
                    break;
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class DivideChunkTask implements Runnable {
        int i;

        public DivideChunkTask(int chunkIndex) {
            this.i = chunkIndex;
        }

        @Override
        public void run() {
            try (FileUtil chunkFileUtil = new FileUtil(getChunkFilePath(i), "rw")) {
                int readSize = getReadSize(i);
                byte[] buf = sourceUtil.read((long) (i - 1) * CHUNK_SIZE, readSize);
                chunkFileUtil.write(0, buf);
                metaFile.setChunkStatus(i, MetaFile.NOT_SEND);

                //async
                uploadChunkPool.execute(new UploadChunkTask(i));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Data
    @AllArgsConstructor
    class UploadChunkTask implements Runnable {

        int i;

        @Override
        public void run() {
            try {
                blockQueue.put(new Block(sourceUtil.getFilePath(), i));
                metaFile.setChunkStatus(i, MetaFile.SENDING);
                //TODO: debug purpose
                //countDownLatch.countDown();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
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

