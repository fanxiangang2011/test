package com.cacss.test.factory.factorymethod;

import com.cacss.test.factory.ICourse;
import com.cacss.test.factory.PythonCourse;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class PythonCourseFactory implements ICourseFactory {
    @Override
    public ICourse create() {
        return new PythonCourse();
    }
}
