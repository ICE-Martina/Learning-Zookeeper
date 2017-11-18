package com.javaman.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:彭哲
 * Date:2017/11/18
 * Curator异步回调
 */
public class AsyncCallback {

    static String path = "/zk-book";
    static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString("119.23.236.253")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    static CountDownLatch countDownLatch = new CountDownLatch(2);
    static ExecutorService tp = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
        client.start();
        System.out.println("Main thread:" + Thread.currentThread().getName());
        //此处传入自定义的Executor
        client.create().creatingParentsIfNeeded().
                withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("event[code:" + curatorEvent.getResultCode() + ",type:" + curatorEvent.getType() + "]");
                System.out.println("Thread of processResult:" + Thread.currentThread().getName());
                countDownLatch.countDown();
            }
        }, tp).forPath(path, "init".getBytes());
        //此处没有传入自定义的Executor
        client.create().creatingParentsIfNeeded().
                withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("event[code:" + curatorEvent.getResultCode() + ",type:" + curatorEvent.getType() + "]");
                System.out.println("Thread of processResult:" + Thread.currentThread().getName());
                countDownLatch.countDown();
            }
        }).forPath(path, "init".getBytes());
        countDownLatch.await();
        tp.shutdown();

    }


}
