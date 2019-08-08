package com.cacss.test.factory.simplefactory;

import com.cacss.test.factory.ICourse;
import com.cacss.test.factory.JavaCourse;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class SimpleFactoryTest {
    public static void main(String[] args) {
        CourseFactory factory=new CourseFactory();
        ICourse course=factory.create(JavaCourse.class);
        course.record();
    }
}
