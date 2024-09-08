package com.hasten.sim;

import lombok.Data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Hasten
 */
@Data
public class SingleFileAcceptor {
    private final BlockingQueue<Block> blockQueue;

    private final BlockingQueue<Block> ackQueue;


    private Executor executor;

    public SingleFileAcceptor(BlockingQueue<Block> blockQueue, BlockingQueue<Block> ackQueue) throws InterruptedException {
        this.blockQueue = blockQueue;
        this.ackQueue = ackQueue;
        this.executor = Executors.newFixedThreadPool(3);

        this.start();
    }


    private void start() throws InterruptedException {
        while (true) {
            Block blk = blockQueue.take();
            System.out.println("taking block: " + blk);
            executor.execute(new HandleFileTask(blk));
        }
    }


    class HandleFileTask implements Runnable {
        private final Block block;

        HandleFileTask(Block block) {
            this.block = block;
        }

        @Override
        public void run() {

            try {
                Thread.sleep(1 * 1000);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Handling block: " + block.getBlockId());
            try {
                ackQueue.put(block);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
