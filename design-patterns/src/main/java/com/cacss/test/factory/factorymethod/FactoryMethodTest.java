package com.cacss.test.factory.factorymethod;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class FactoryMethodTest {

    public static void main(String[] args) {
        ICourseFactory factory=new JavaCourseFactory();
        factory.create().record();
        factory=new PythonCourseFactory();
        factory.create().record();
    }
}
