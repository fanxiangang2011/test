package com.cacss.test.factory.abstractfactory;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class AbstractFactoryTest {
    public static void main(String[] args) {
        JavaCourseFactory javaCourseFactory=new JavaCourseFactory();
        javaCourseFactory.createNote().edit();
        javaCourseFactory.createVideo().record();
        PythonCourseFactory pythonCourseFactory=new PythonCourseFactory();
        pythonCourseFactory.createNote().edit();
        pythonCourseFactory.createVideo().record();
    }
}
