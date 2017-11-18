package com.javaman.zookeeper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Author:彭哲
 * Date:2017/11/18
 * 无锁,出现并发问题
 */
public class NoLock {

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNO = format.format(new Date());
                    System.out.println("生成的订单号:" + orderNO);
                }
            }).start();
        }
        countDownLatch.countDown();
    }


}
