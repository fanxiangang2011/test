package com.cacss.test.factory.simplefactory;

import com.cacss.test.factory.ICourse;

/**
 * Created by Fanxiangang on 2019/8/8.
 */
public class CourseFactory {

    public ICourse create(Class<? extends ICourse> clazz){
        if(null !=clazz){
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
