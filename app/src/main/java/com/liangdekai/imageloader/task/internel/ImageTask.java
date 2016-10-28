package com.liangdekai.imageloader.task.internel;

import com.liangdekai.imageloader.task.TaskChain;

/**
 * Created by asus on 2016/8/7.
 */
public class ImageTask implements Runnable {
    private TaskChain mTaskChain ;

    public ImageTask (TaskChain taskChain ){
        mTaskChain = taskChain ;
    }

    @Override
    public void run() {
        mTaskChain.handleTask();
    }
}
