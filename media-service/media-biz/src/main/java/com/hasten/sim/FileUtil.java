package com.hasten.sim;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Hasten
 */
@Data
public class FileUtil implements AutoCloseable {


    private File file;
    private RandomAccessFile raf;
    private FileChannel channel;
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();


    public FileUtil(String filePath) throws IOException {
        this(filePath, "rw");
    }

    public FileUtil(String filePath, String mode) throws IOException {
        this.file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        this.raf = new RandomAccessFile(file, mode);
        this.channel = raf.getChannel();
    }

    public FileUtil(File file, String mode) throws IOException {
        this.file = file;
        this.raf = new RandomAccessFile(file, mode);
        this.channel = raf.getChannel();
    }

    public long getChunkCount(long chunkSize) {
        return (long) Math.ceil(((double) file.length() / chunkSize));
    }

    public long getLength() {
        return file.length();
    }

    public void write(long begin, byte[] bytes) throws IOException, InterruptedException {
        rwLock.writeLock().lock();
        try {
            channel.write(ByteBuffer.wrap(bytes), begin);
        }
        finally {
            rwLock.writeLock().unlock();
        }
    }

    public void writeLong(long begin, long value) {
        try {
            byte[] bytes = Parser.long2Byte(value);
            this.write(begin, bytes);
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeInt(long begin, int value) {
        try {
            byte[] bytes = Parser.int2Byte(value);
            this.write(begin, bytes);
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeByte(long begin, byte value) {
        try {
            this.write(begin, new byte[]{value});
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] read(long begin, long size) throws IOException {
        rwLock.readLock().lock();
        try {
            byte[] bytes = new byte[(int) size];
            channel.read(ByteBuffer.wrap(bytes), begin);
            return bytes;
        }
        finally {
            rwLock.readLock().unlock();
        }
    }


    public long readLong(long begin) {
        rwLock.readLock().lock();
        try {
            byte[] longBytes = this.read(begin, Long.BYTES);
            return Parser.parseLong(longBytes);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            rwLock.readLock().unlock();
        }
    }

    public int readInt(long begin) {
        rwLock.readLock().lock();
        try {
            byte[] intBytes = this.read(begin, Integer.BYTES);
            return Parser.parseInt(intBytes);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            rwLock.readLock().unlock();
        }
    }

    public byte readByte(long begin) {
        rwLock.readLock().lock();
        try {
            byte[] byteBytes = this.read(begin, Byte.BYTES);
            return byteBytes[0];
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            rwLock.readLock().unlock();
        }
    }


    @Override
    public void close() throws Exception {
        if (channel != null) {
            channel.close();
        }
        if (raf != null) {
            raf.close();
        }
    }

    public String getFilePath() {
        return file.getPath();
    }
}
