package com.javaman.zookeeper.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author pengzhe
 * @date 2018/4/18 21:10
 * @description
 */

public class CuratorLockThread {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            //启动10个线程模拟多个客户端
            JvmLock jl = new JvmLock(i);
            new Thread(jl).start();
            //这里加上300毫秒是为了让线程按顺序启动，不然有可能4号线程比3号线程先启动了，这样测试就不准了。
            Thread.sleep(100);
        }
    }

    public static class JvmLock implements Runnable {
        /**
         * zookeeper地址
         */
        static final String CONNECT_ADDR = "10.60.96.142:2182";
        /**
         * session超时时间
         */
        static final int SESSION_OUTTIME = 5000;//ms
        private int num;

        public JvmLock(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
            CuratorFramework cf = CuratorFrameworkFactory.builder()
                    .connectString(CONNECT_ADDR)
                    .sessionTimeoutMs(SESSION_OUTTIME)
                    .retryPolicy(retryPolicy)
                    .build();
            cf.start();
            InterProcessMutex lock = new InterProcessMutex(cf, "/mylock");
            try {
                System.out.println("我是第" + num + "号线程，我开始获取锁");
                lock.acquire();
                System.out.println("我是第" + num + "号线程，我已经获取锁");
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    lock.release();
                    System.out.println("我是第" + num + "号线程,我已经释放锁");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cf.close();
        }
    }

}
