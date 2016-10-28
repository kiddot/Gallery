package com.liangdekai.imageloader.task.internel;

import android.graphics.Bitmap;

import com.liangdekai.imageloader.task.TaskChain;

/**
 * Created by asus on 2016/9/4.
 */
public interface BaseTask {
    Bitmap handle(TaskChain chain);
}
