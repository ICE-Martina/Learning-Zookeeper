package com.javaman.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Author:彭哲
 * Date:2017/11/18
 * DistributedBarrier
 * barrier是一种用来控制多线程之间同步的经典方式
 */
public class CuratorDistributedBarrier {

    static String barrier_path = "/curator_barrier_path";
    static DistributedBarrier barrier;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory.builder()
                                .connectString("119.23.236.253:2181")
                                .sessionTimeoutMs(50000)
                                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
                        client.start();
                        barrier = new DistributedBarrier(client, barrier_path);
                        System.out.println(Thread.currentThread().getName() + "号barrier设置");
                        //设置barrier
                        barrier.setBarrier();
                        //等待barrier释放
                        barrier.waitOnBarrier();
                        System.out.println("启动");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(2000);
        //释放barrier
        barrier.removeBarrier();
    }
}
