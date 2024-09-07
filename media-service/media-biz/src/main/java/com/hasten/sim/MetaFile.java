package com.hasten.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.hasten.sim.SingleFileUploader.CHUNK_SIZE;

/**
 * Blk, also the chunk!!!!!
 * @author Hasten
 */
public class MetaFile {

    public static final byte NOT_CHUNK = -2;
    public static final byte NOT_SEND = -1;
    public static final byte SENDING = -0;
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
            this.setBlkAck(i, NOT_SEND);
        }
    }

    public void setBlkAck(int blkNum, byte ack) {
        metaFileUtil.writeByte(getBlkNumOffset(blkNum), ack);
    }


    private long getBlkNumOffset(int blkNum) {
        return FIRST_BLK_ACK_OFFSET + blkNum - 1;
    }

    //for debug
    public void readMetaFile() {
        long fileSize = metaFileUtil.readLong(FILE_SIZE_OFFSET);
        System.out.println("fileSize: " + fileSize);

        int blkNum = metaFileUtil.readInt(BLK_NUM_OFFSET);
        System.out.println("blkNum: " + blkNum);

        for (int i = 1; i <= blkNum; i++) {
            byte blkAck = metaFileUtil.readByte(getBlkNumOffset(i));
            System.out.println("blk" + i + "Ack: " + blkAck);
        }
    }

    public byte checkBlkAck(int blkNum) {
        return metaFileUtil.readByte(getBlkNumOffset(blkNum));
    }

    public List<Integer> getNotAckedBlkIdList() {
        List<Integer> notAckedBlkIdList = new ArrayList<>();
        int blkNum = metaFileUtil.readInt(BLK_NUM_OFFSET);
        for (int i = 1; i <= blkNum; i++) {
            byte blkAck = metaFileUtil.readByte(getBlkNumOffset(i));
            if (blkAck != ACKED) {
                notAckedBlkIdList.add(i);
            }
        }
        Collections.sort(notAckedBlkIdList);
        return notAckedBlkIdList;
    }
}
