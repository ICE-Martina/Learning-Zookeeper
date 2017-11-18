package com.javaman.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Author:彭哲
 * Date:2017/11/18
 * 分布式锁
 * InterProcessLock
 */
public class CuratorDistributedLock {


    static String lock_path = "/curator_distributed_lock";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("119.23.236.253:2181")
            .sessionTimeoutMs(50000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 30; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        lock.acquire();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNO = format.format(new Date());
                    System.out.println("生成的订单号：" + orderNO);
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        countDownLatch.countDown();
    }


}
