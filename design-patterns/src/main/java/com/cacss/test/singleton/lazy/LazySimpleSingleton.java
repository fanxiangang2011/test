package com.cacss.test.singleton.lazy;

/**
 * Created by Fanxiangang on 2019/8/8.
 * 懒汉式单例
 */
public class LazySimpleSingleton {
    public LazySimpleSingleton() {}
    private  static LazySimpleSingleton lazy=null;

    public synchronized static LazySimpleSingleton getInstance(){
        if(lazy==null){
            lazy=new LazySimpleSingleton();
        }
        return lazy;
    }
}
