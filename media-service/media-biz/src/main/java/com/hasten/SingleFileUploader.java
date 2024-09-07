package com.hasten;


import lombok.var;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Hasten
 */
public class SingleFileUploader {
    private File sourceFile;
    private File targetFile;

    private Executor threadPool;
    private RandomAccessFile sourceRaf;

    private long chunkCount;

    private String chunkDirPath;

    private static final int CHUNK_SIZE = 1024 * 1024 * 1;//1MB

    public SingleFileUploader(String filePath) throws FileNotFoundException {
        this.sourceFile = new File(filePath);
        this.targetFile = new File("target-" + filePath);
        this.sourceRaf = new RandomAccessFile(sourceFile, "r");
        this.threadPool = Executors.newFixedThreadPool(3);
        this.chunkCount = (long) Math.ceil((double) sourceFile.length() / CHUNK_SIZE);
        this.chunkDirPath = sourceFile.getPath() + ".chunks";
    }

    public void uploadFile() throws IOException {

        File chunkDir = new File(chunkDirPath);
        if (!chunkDir.exists()) {
            chunkDir.mkdir();
        }

        for (int i = 1; i <= chunkCount; i++) {
            File chunkFile = new File(this.getChunkFilePath(i));
            if (chunkFile.exists()) {
                chunkFile.delete();
            }
            chunkFile.createNewFile();

            byte[] buf;
            if (i == chunkCount) {
                buf = new byte[(int) (sourceFile.length() % CHUNK_SIZE)];
            }
            else {
                buf = new byte[CHUNK_SIZE];
            }
            sourceRaf.seek((long) (i - 1) * CHUNK_SIZE);
            sourceRaf.read(buf);

            try (var chunkRaf = new RandomAccessFile(chunkFile, "rw");) {
                chunkRaf.write(buf);
            }
        }
    }


    public void mergeChunk() throws IOException {
        if (!this.targetFile.exists()) {
            this.targetFile.createNewFile();
        }

        RandomAccessFile targetRaf = new RandomAccessFile(this.targetFile, "rw");

        File chunkDir = new File(chunkDirPath);
        File[] chunkList = chunkDir.listFiles();
        if (chunkList == null || chunkList.length == 0) {
            throw new IOException("No chunk files found in " + chunkDirPath);
        }
        Arrays.sort(chunkList, Comparator.comparing(File::getPath));

        for (int i = 1; i <= chunkList.length; i++) {
            try (var chunkRaf = new RandomAccessFile(chunkList[i - 1], "r")) {
                byte[] buf;
                if (i == chunkList.length) {
                    buf = new byte[(int) (this.sourceFile.length() % CHUNK_SIZE)];
                }
                else {
                    buf = new byte[CHUNK_SIZE];
                }

                chunkRaf.read(buf);

                targetRaf.seek((long) (i - 1) * CHUNK_SIZE);
                targetRaf.write(buf);
            }
        }

    }


    private String getChunkFilePath(int chunkIndex) {
        return chunkDirPath + File.separator + chunkIndex + ".chunk";
    }
}

