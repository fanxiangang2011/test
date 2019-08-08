package com.cacss.test.singleton.lazy;

/**
 * Created by Fanxiangang on 2019/8/8.
 * 懒汉式单例
 * 这种形式兼顾饿汉式的内存浪费，已兼顾了synchronized性能问题
 * 完美的屏蔽了这两个缺点
 * 世上最牛B的单例模式的实现方式
 */
public class LazyInnerClassSingleton {
    //默认使用LazyInnerClassSingleton的，会先初始化内部类
   //如果没有使用的话，内部类是不加载的
    public LazyInnerClassSingleton() {
        if(LazyHolder.LAZY !=null){
            throw new RuntimeException("不允许创建多个实例");
        }
    }

    /**
     * 每个关键字都不是多余的
     * static 是为了使单例的空间共享
     * 保证这个方法不会被重写，重载
     * @return
     */
    public static final LazyInnerClassSingleton getInstance(){
        return LazyHolder.LAZY;
    }

   private static class LazyHolder{
        private static final LazyInnerClassSingleton LAZY=new LazyInnerClassSingleton();
   }
}
