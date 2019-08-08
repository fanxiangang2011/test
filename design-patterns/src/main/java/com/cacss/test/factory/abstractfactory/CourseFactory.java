package com.cacss.test.factory.abstractfactory;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public interface CourseFactory {
    INote createNote();
    IVideo createVideo();
}
