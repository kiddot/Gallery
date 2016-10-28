package com.liangdekai.imageloader.task.internel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.liangdekai.imageloader.listener.TaskListener;
import com.liangdekai.imageloader.bean.ViewHolder;

public class CompressTask implements BaseImageTask {
    private int mWidth ;
    private int mHeight ;
    private ImageView mImageView ;
    private String mPath ;
    private TaskListener mTaskListener ;

    public CompressTask(TaskListener taskListener , String path , int width , int height , ImageView imageView){
        mTaskListener = taskListener ;
        mPath = path ;
        mWidth = width ;
        mHeight = height ;
        mImageView = imageView ;
    }

    /**
     * 计算采样率
     * @param options
     * @param requireHeight
     * @param requireWidth
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options , int requireHeight , int requireWidth){
        final int height = options.outHeight ;
        final int width = options.outWidth ;
        int inSampleSize = 1 ;
        if (height > requireHeight || width > requireWidth){
            final int heightRatio = (Math.round((float)height / (float) requireHeight));
            final int widthRatio = (Math.round((float)width / (float) requireWidth));
            inSampleSize = heightRatio > widthRatio ? widthRatio : heightRatio ;
        }
        return inSampleSize;
    }

    /**
     * 根据采样率对图片进行压缩
     * @param path
     * @param requireHeight
     * @param requireWidth
     * @return
     */
    public static Bitmap compressImage(String path, int requireHeight , int requireWidth){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true ;
        BitmapFactory.decodeFile(path ,options);
        options.inSampleSize = calculateInSampleSize(options , requireHeight , requireWidth);
        options.inJustDecodeBounds = false ;
        return BitmapFactory.decodeFile(path , options);
    }

    @Override
    public void handleTask() {
        ViewHolder viewHolder = new ViewHolder() ;
        viewHolder.bitmap = compressImage(mPath , mHeight , mWidth) ;
        viewHolder.path = mPath ;
        viewHolder.imageView = mImageView ;
        mTaskListener.onFinish(viewHolder);
    }
}
