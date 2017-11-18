package com.javaman.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Author:彭哲
 * Date:2017/11/18
 * 事件监听
 * <p>
 * 原生zk支持通过反复注册Watcher来进行事件监听,使用一次便失效了,需要开发人员反复注册watcher,比较繁琐
 * Curator引进了Cache来实现对zk服务端事件的监听.
 * Cache是Curator中对事件类型的包装,其对事件的监听其实可以看做是一个本地缓存视图和远程zk视图的对比过程,
 * 同时curator能够为开发人员处理反复注册监听,从而大大简化了开发
 * Cache分两种类型:节点监听和子节点监听
 */
public class CuratorNodeCache {

    static String path = "/zk-book/nodecache";
    static CuratorFramework client = CuratorFrameworkFactory
            .builder()
            .connectString("119.23.236.253:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, "init".getBytes());
        final NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("Node data update,new data:" + new String(nodeCache.getCurrentData().getData()));
            }
        });
        client.setData().forPath(path, "u".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }


}
