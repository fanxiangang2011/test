package com.cacss.test.singleton.register;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public enum EnumSingleton {

    INSTANCE;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}
