package io.github.kingschan1204.istock.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenguoxiang
 * @create 2019-03-07 11:13
 **/
public class MyThreadFactory implements ThreadFactory {

    private AtomicInteger counter = new AtomicInteger(0);
    private String name;

    public MyThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, name + "-t-" + counter);
        counter.incrementAndGet();
        return t;
    }
}
