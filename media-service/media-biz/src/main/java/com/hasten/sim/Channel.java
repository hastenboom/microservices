package com.hasten.sim;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Hasten
 */
public class Channel {

    public static final BlockingQueue<Block> blockQueue = new ArrayBlockingQueue<>(10);

    public static final BlockingQueue<Block> ackQueue = new ArrayBlockingQueue<>(10);
}
