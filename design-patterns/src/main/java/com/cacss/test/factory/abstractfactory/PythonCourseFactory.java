package com.cacss.test.factory.abstractfactory;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class PythonCourseFactory implements CourseFactory {
    @Override
    public INote createNote() {
        return new PythonNote();
    }

    @Override
    public IVideo createVideo() {
        return new PythonVideo();
    }
}
