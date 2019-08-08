package com.cacss.test.singleton.threadlocal;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class ThreadLocalSingleton {
    public ThreadLocalSingleton() {
    }
    public static final ThreadLocal<ThreadLocalSingleton> threadLocalInstance=
            new ThreadLocal<ThreadLocalSingleton>(){
                @Override
                protected ThreadLocalSingleton initialValue() {
                    return new ThreadLocalSingleton();
                }
            };
    public static ThreadLocalSingleton getInstance(){
        return threadLocalInstance.get();
    }
}
