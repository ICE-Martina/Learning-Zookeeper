package com.javaman.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * Author:彭哲
 * Date:2017/11/18
 * 分布式计数器
 * 指定一个zk数据节点作为计数器,多个应用实例在分布式锁的控制下,
 * 通过该数据节点的内容来实现计数功能
 */
public class CuratorDistributedCount {

    static String distributed_count = "/curator_distributed_count";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("119.23.236.253:2181")
            .sessionTimeoutMs(50000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws Exception {
        client.start();
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, distributed_count,
                new RetryNTimes(3, 10));
        AtomicValue<Integer> atomicValue = atomicInteger.add(8);
        System.out.println("Result:" + atomicValue.succeeded());
    }


}
