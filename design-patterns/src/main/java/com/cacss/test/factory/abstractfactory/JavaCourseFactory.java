package com.cacss.test.factory.abstractfactory;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class JavaCourseFactory implements CourseFactory {
    @Override
    public INote createNote() {
        return new JavaNote();
    }

    @Override
    public IVideo createVideo() {
        return new JavaVideo();
    }
}
