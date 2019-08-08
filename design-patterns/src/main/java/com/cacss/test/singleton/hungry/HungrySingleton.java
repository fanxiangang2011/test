package com.cacss.test.singleton.hungry;

/**
 * Created by Fanxiangang on 2019/8/8.
 * 懒汉式是单例
 * 它是类加载的时候就立即初始化，并且创建单例对象
 * 优点：没有任何的锁，执行效率比较高，在用户体验上来说，比懒汉式更好
 * 缺点：类加载的时候就初始化，不管你用还是不用，我都占着空间，浪费了内存，有可能站着茅坑不拉屎
 *
 * 绝对线程安全，在线程还没有出现以前，就实例化了，不可能存在访问安全问题
 * 执行的过程：
 * //先静态，后动态
 * //先属性，后方法
 * //先上后下
 */
public class HungrySingleton {

    private  static final HungrySingleton hungrySingleton=new HungrySingleton();

    public HungrySingleton() {
    }
    public static HungrySingleton getInstance(){
        return hungrySingleton;
    }
}
