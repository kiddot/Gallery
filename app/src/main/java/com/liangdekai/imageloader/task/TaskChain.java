package com.liangdekai.imageloader.task;

import com.liangdekai.imageloader.task.internel.BaseImageTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/7.
 */
public class TaskChain implements BaseImageTask {
    private List<BaseImageTask> mImageTaskList ;

    public TaskChain(){
        mImageTaskList = new ArrayList<BaseImageTask>();
    }

    public void addToTaskChain(BaseImageTask imageTask){
        mImageTaskList.add(imageTask) ;
    }

    @Override
    public void handleTask() {
        for (BaseImageTask imageTask : mImageTaskList){
            imageTask.handleTask();
        }
    }
}
