package com.hasten;

import com.hasten.sim.Block;
import com.hasten.sim.Channel;
import com.hasten.sim.SingleFileAcceptor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Hasten
 */
public class ServerTest {

    @Test
    public void testServer() throws InterruptedException {

        SingleFileAcceptor singleFileAcceptor = new SingleFileAcceptor(Channel.blockQueue, Channel.ackQueue);
    }



}
