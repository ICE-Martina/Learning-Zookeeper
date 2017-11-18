package com.javaman.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Author:彭哲
 * Date:2017/11/18
 * Master选举
 * <p>
 * 思路:
 * 选择一个根节点,例如/master_select,多台机器同时向该节点创建子节点/master_select/lock
 * ,利用zookeeper的特性,最终只有一台机器能够创建成功,成功的那台就是Master
 */
public class CuratorMasterSelect {

    static String master_path = "/curator_master_select";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("119.23.236.253:2181")
            .sessionTimeoutMs(50000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws InterruptedException {
        client.start();
        LeaderSelector leaderSelector = new LeaderSelector(client, master_path,
                //curator会在成功获取master权利后回调该监听器,可以在此方法中实现业务逻辑
                new LeaderSelectorListenerAdapter() {
            //一旦执行完takeLeaderShip方法,curator会立即释放master权利,重新开始新一轮的选举
                    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                        System.out.println("成为master角色");
                        Thread.sleep(300);
                        System.out.println("完成master操作,释放master权利");
                    }
                });
        leaderSelector.autoRequeue();
        leaderSelector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }


}
