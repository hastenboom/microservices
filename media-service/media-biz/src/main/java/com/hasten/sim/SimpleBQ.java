package com.hasten.sim;

import java.util.LinkedList;

/**
 * @author Hasten
 */

public class SimpleBQ<T> {
    private final LinkedList<T> queue;
    private final int capacity;

    public SimpleBQ(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    // 生产者方法：向队列中添加元素
    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // 队列已满，等待消费者消费
        }
        queue.add(item);
        notifyAll(); // 通知消费者队列中有新的元素
    }

    // 消费者方法：从队列中取出元素
    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // 队列为空，等待生产者生产
        }
        T item = queue.removeFirst();
        notifyAll(); // 通知生产者队列有空间了
        return item;
    }

    // 打印队列当前状态的方法
    public synchronized void printQueueStatus() {
        System.out.print("Current Queue Status: [");
        for (int i = 0; i < queue.size(); i++) {
            System.out.print(queue.get(i));
            if (i < queue.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
