package com.javaman.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Author:彭哲
 * Date:2017/11/17
 */
public class CuratorClient {

    public static void main(String[] args) throws InterruptedException {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("119.23.236.253:2181", 5000, 3000, retryPolicy);
        client.start();

        Thread.sleep(Integer.MAX_VALUE);

    }

}
