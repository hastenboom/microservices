package com.hasten.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.hasten.sim.SingleFileUploader.CHUNK_SIZE;

/**
 * Blk, also the chunk!!!!!
 * @author Hasten
 */
public class MetaFile {

    public static final byte NOT_CHUNK = -2;
    public static final byte NOT_SEND = -1;
    public static final byte SENDING = 0;
    public static final byte ACKED = 1;

    public static final long FILE_SIZE_OFFSET = 0;
    public static final long BLK_NUM_OFFSET = 8;
    public static final long FIRST_BLK_ACK_OFFSET = 12;

    FileUtil srcFileUtil;
    FileUtil metaFileUtil;

    public static final String META_FILE_PREFIX = "meta-";

    public MetaFile(String filePath) throws IOException {
        this(new FileUtil(filePath));
    }

    public MetaFile(FileUtil srcFileUtil) throws IOException {
        this.srcFileUtil = srcFileUtil;
        this.metaFileUtil = new FileUtil(META_FILE_PREFIX + srcFileUtil.getFilePath());
        this.prepareAndWriteMetaData();
    }

    /*
     * |fileSize|blkNum|blk1Ack|blk2Ack|...|blkNAck|
     * |8B      |4B    |1B     |1B     |...|1B     |
     * */
    private void prepareAndWriteMetaData() {
        metaFileUtil.writeLong(FILE_SIZE_OFFSET, srcFileUtil.getLength());
        metaFileUtil.writeInt(BLK_NUM_OFFSET, (int) srcFileUtil.getChunkCount(CHUNK_SIZE));
        for (int i = 1; i <= (int) srcFileUtil.getChunkCount(CHUNK_SIZE); i++) {
            this.setChunkStatus(i, NOT_CHUNK);
        }
    }

    public void setChunkStatus(int blkNum, byte status) {
        metaFileUtil.writeByte(getChunkOffset(blkNum), status);
    }


    private long getChunkOffset(int blkNum) {
        return FIRST_BLK_ACK_OFFSET + blkNum - 1;
    }

    //for debug
    public void printMetaFile() {
        long fileSize = metaFileUtil.readLong(FILE_SIZE_OFFSET);
        System.out.println("fileSize: " + fileSize);

        int blkNum = metaFileUtil.readInt(BLK_NUM_OFFSET);
        System.out.println("blkNum: " + blkNum);

        for (int i = 1; i <= blkNum; i++) {
            byte blkAck = metaFileUtil.readByte(getChunkOffset(i));
            System.out.println("blk" + i + "Ack: " + blkAck);
        }
    }

    public byte checkChunkStatus(int blkNum) {
        return metaFileUtil.readByte(getChunkOffset(blkNum));
    }

    public List<Integer> getStatusList(Predicate<Byte> predicate) {
        List<Integer> ids = new ArrayList<>();
        int blkNum = metaFileUtil.readInt(BLK_NUM_OFFSET);
        for (int i = 1; i <= blkNum; i++) {
            byte blkStatus = metaFileUtil.readByte(getChunkOffset(i));
            if (predicate.test(blkStatus)) {
                ids.add(i);
            }
        }
        return ids;
    }

    public List<Integer> getNotChuckIdList() {
        return this.getStatusList((b) -> b == NOT_CHUNK);
    }

    public List<Integer> getNotSendIdList() {
        return this.getStatusList((b) -> b == NOT_SEND);
    }

    public List<Integer> getSendingIdList() {
        return this.getStatusList((b) -> b == SENDING);
    }

    public List<Integer> getAckedIdList() {
        return this.getStatusList((b) -> b == ACKED);
    }

    public List<Integer> getNotSendAndSendingIdList() {
        return this.getStatusList((b) -> b == NOT_SEND || b == SENDING);
    }

    public boolean uploadIsDone() {
//        return getNotSendIdList().isEmpty() && getNotSendIdList().isEmpty() && getSendingIdList().isEmpty();

        return getAckedIdList().size() == (int) srcFileUtil.getChunkCount(CHUNK_SIZE);
    }
}
