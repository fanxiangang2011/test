package com.cacss.test.singleton.lazy;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class LazyDoubleCheckSingleton {
    public LazyDoubleCheckSingleton() {
    }
    private volatile static LazyDoubleCheckSingleton lazy=null;

    public static LazyDoubleCheckSingleton getInstance(){
        if(lazy==null){
            synchronized (LazyDoubleCheckSingleton.class){
                if(lazy==null){
                    lazy=new LazyDoubleCheckSingleton();
                }
            }
        }
        return lazy;
    }
}
