package com.javaman.zookeeper;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:彭哲
 * Date:2017/11/18
 * JDK自带CycliBarrier
 * 多线程并发情况下,都会准确的等待所有的线程都处于就绪状态后才会开始同时执行其他的业务逻辑
 * 同一个JVM中可以使用CycliBarrier,分部署环境中使用DistributedBarrier
 * 模拟赛跑演示CycliBarrier
 */
public class JDKBarrier {

    public static CyclicBarrier barrier = new CyclicBarrier(3);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new Thread(new Runner("1号选手")));
        executorService.submit(new Thread(new Runner("2号选手")));
        executorService.submit(new Thread(new Runner("3号选手")));
        executorService.shutdown();
    }

}

class Runner implements Runnable {
    private String name;

    public Runner(String name) {
        this.name = name;
    }

    public void run() {
        System.out.println(name + "准备好了");
        try {
            JDKBarrier.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(name + "起跑!!");
    }

}