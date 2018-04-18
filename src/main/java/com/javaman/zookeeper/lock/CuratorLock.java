package com.javaman.zookeeper.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author pengzhe
 * @date 2018/4/18 21:07
 * @description
 */

public class CuratorLock {
    /**
     * zookeeper地址
     */
    static final String CONNECT_ADDR = "10.60.96.142:2182";
    /**
     * session超时时间
     */
    static final int SESSION_OUTTIME = 5000;//ms

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_OUTTIME)
                .retryPolicy(retryPolicy)
                .build();
        cf.start();
        InterProcessMutex lock = new InterProcessMutex(cf, "/mylock");
        try {
            lock.acquire();
            System.out.println("已经获取到锁");
            Thread.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release();
            System.out.println("锁已释放");
        }
        Thread.sleep(10000);
        cf.close();
    }
}
