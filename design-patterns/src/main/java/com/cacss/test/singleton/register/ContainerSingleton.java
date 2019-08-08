package com.cacss.test.singleton.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class ContainerSingleton {
    public ContainerSingleton() {}
    private static Map<String,Object> ioc=new ConcurrentHashMap<String,Object>();
    public static Object getInstance(String className){
        synchronized(ioc){
            if(!ioc.containsKey(className)){
                Object obj=null;
                try {
                    obj=Class.forName(className).newInstance();
                    ioc.put(className,obj);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return obj;
            }else{
                return ioc.get(className);
            }
        }
    }
}
