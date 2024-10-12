package com.yb.yff.sb.jvm.threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright (c) 2024 to 2045  YangBo.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * YangBo. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with YangBo.
 *
 * @author : YangBo
 * @Project: SpringbootDemo
 * @Class: LockTimeoutExample
 * @CreatedOn 2024/9/23.
 * @Email: yangboyff@gmail.com
 * @Description: LockTimeoutExample
 */
public class LockTimeoutExample {
    private final Lock lock = new ReentrantLock();

    // A线程执行的任务
    public void doTaskA() {
        lock.lock();  // A线程获取锁
        try {
            System.out.println(Thread.currentThread().getName() + " has acquired the lock and is working.");
            // 模拟长时间任务，A线程占用锁
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();  // 释放锁
            System.out.println(Thread.currentThread().getName() + " has released the lock.");
        }
    }

    // B线程执行的任务，尝试获取锁，并等待一段时间
    public void doTaskB() {
        try {
            // 尝试在3秒内获取锁，超过时间则放弃
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    System.out.println(Thread.currentThread().getName() + " has acquired the lock and is working.");
                    // 模拟短任务
                    Thread.sleep(1000);
                } finally {
                    lock.unlock();  // 释放锁
                    System.out.println(Thread.currentThread().getName() + " has released the lock.");
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire the lock within the time limit.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LockTimeoutExample example = new LockTimeoutExample();

        // 创建线程A，执行长时间任务，获取锁
        Thread threadA = new Thread(example::doTaskA, "Thread A");
        threadA.start();

        // 创建线程B，尝试获取锁，等待指定时间后放弃
        Thread threadB = new Thread(example::doTaskB, "Thread B");
        threadB.start();
    }
}
