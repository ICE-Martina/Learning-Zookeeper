package com.javaman.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Author:彭哲
 * Date:2017/11/17
 * Curator客户端
 */
public class CuratorClient {

    public static void main(String[] args) throws Exception {

        String path1 = "/zk-book/c1";
        String path2 = "/zk-book/c3";

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        Fluent风格
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("119.23.236.253:2181")
                .sessionTimeoutMs(50000)
                .retryPolicy(retryPolicy).build();
        client.start();
        //创建节点
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path2, "init".getBytes());
        //删除节点
        Stat stat=new Stat();
        client.getData().storingStatIn(stat).forPath(path1);
        client.delete().deletingChildrenIfNeeded()
                .withVersion(stat.getVersion()).forPath(path1);

        //获取节点数据
        System.out.println(client.getData().storingStatIn(stat).forPath(path2));

        //更新节点数据 setData()


    }

}
