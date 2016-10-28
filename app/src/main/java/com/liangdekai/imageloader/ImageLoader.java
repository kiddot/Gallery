package com.liangdekai.imageloader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import com.liangdekai.imageloader.manager.CacheManager;
import com.liangdekai.imageloader.task.internel.CompressTask;
import com.liangdekai.imageloader.task.internel.ImageTask;
import com.liangdekai.imageloader.task.TaskChain;
import com.liangdekai.imageloader.listener.TaskListener;
import com.liangdekai.imageloader.manager.TaskManager;
import com.liangdekai.imageloader.bean.ViewHolder;

/**
 * 该类用于设置图片的显示
 */

public class ImageLoader implements TaskListener {
    private static final int COMPRESS_SIZE = 100;
    private static final int COMPRESS_BIG_SIZE = 800;
    private static ImageLoader mImageLoader;
    private TaskManager mTaskManager;
    private CacheManager mCacheManager ;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            ViewHolder dataHolder = (ViewHolder) msg.obj;
            ImageView image = dataHolder.imageView;
            String imagePath = dataHolder.path;
            Bitmap imageBitmap = dataHolder.bitmap;
            if (image.getTag().toString().equals(imagePath)){
                image.setImageBitmap(imageBitmap);
            }
        }
    };

    /**
     * 获取该类的实例
     * @return
     */
    public static ImageLoader getInstance(){
        if (mImageLoader == null){
            synchronized (ImageLoader.class){
                if (mImageLoader == null){
                    mImageLoader = new ImageLoader();
                }
            }
        }
        return mImageLoader;
    }

    public ImageLoader(){
        mTaskManager = TaskManager.getInstance() ;
        mCacheManager = CacheManager.getInstance() ;
    }

    /**
     * 为每个Item设置图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path , final ImageView imageView){
        ViewHolder holder = new ViewHolder();
        final Bitmap bitmap= mCacheManager.getFromLruCache(path);
        if (bitmap != null){
            holder.bitmap = bitmap;
            holder.imageView = imageView;
            holder.path = path ;
            Message message = Message.obtain();
            message.obj = holder ;
            mHandler.sendMessage(message);
        }else{
            TaskChain taskChain = new TaskChain();
            taskChain.addToTaskChain(new CompressTask(this , path , COMPRESS_SIZE , COMPRESS_SIZE , imageView));
            mTaskManager.addLoadTask(new ImageTask(taskChain));

            /*mTaskManager.addLoadTask(new Runnable() {
                @Override
                public void run() {//任务详情
                    Holder holder = new Holder();
                    Bitmap bm = CompressImage.compressImage(path , COMPRESS_SIZE , COMPRESS_SIZE);//压缩图片
                    mCacheManager.addToLruCache(path , bm);//添加到缓存
                    holder.bitmap = bm;
                    holder.imageView = imageView;
                    holder.path = path ;
                    Message message = Message.obtain();
                    message.obj = holder ;
                    mHandler.sendMessage(message);
                    mTaskManager.semaphore.release();//释放信号量
                }
            });*/
        }
    }

    /**
     * 加载压缩的大图
     * @param path
     * @param imageView
     */
    public void loadLargeImage(String path ,ImageView imageView){
        Bitmap bitmap= mCacheManager.getFromLruCache(path);
        if (imageView != null){
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else {
                TaskChain taskChain = new TaskChain();
                taskChain.addToTaskChain(new CompressTask(this , path , COMPRESS_SIZE , COMPRESS_SIZE , imageView));
                mTaskManager.addLoadTask(new ImageTask(taskChain));
                /*imageView.setImageResource(R.mipmap.empty);
                Bitmap bm = CompressImage.compressImage(path , COMPRESS_SIZE , COMPRESS_SIZE);//压缩图片
                mCacheManager.addToLruCache(path , bm);*/
            }
        }
        //MemoryTask
        //DiskMemoryTask
        //CompressTask

        //Bitmap
    }

    /**
     * 加载原图
     * @param path
     * @param imageView
     */
    public void loadImageDetail(final String path , final ImageView imageView){
        TaskChain taskChain = new TaskChain();
        taskChain.addToTaskChain(new CompressTask(this , path , COMPRESS_BIG_SIZE , COMPRESS_BIG_SIZE , imageView));
        mTaskManager.addLoadTask(new ImageTask(taskChain));
        /*mTaskManager.addLoadTask(new Runnable() {
            @Override
            public void run() {
                /*ViewHolder holder = new ViewHolder();
                holder.bitmap = CompressImage.compressImage(path , COMPRESS_BIG_SIZE , COMPRESS_BIG_SIZE);
                holder.imageView = imageView;
                holder.path = path ;
                Message message = Message.obtain();
                message.obj = holder ;
                mHandler.sendMessage(message);
                mTaskManager.semaphore.release();//释放信号量
            }
        });*/
    }

    @Override
    public void onFinish(ViewHolder viewHolder) {
        mCacheManager.addToLruCache(viewHolder.path , viewHolder.bitmap);//添加到缓存
        Message message = Message.obtain();
        message.obj = viewHolder ;
        mHandler.sendMessage(message);
        mTaskManager.semaphore.release();//释放信号量
    }
}
