package com.cacss.test.factory.abstractfactory;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class JavaVideo implements IVideo {
    @Override
    public void record() {
        System.out.println("录制Java课程");
    }
}
