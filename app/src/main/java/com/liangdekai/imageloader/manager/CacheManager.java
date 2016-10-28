package com.liangdekai.imageloader.manager;

import android.graphics.Bitmap;
import android.util.LruCache;

public class CacheManager {
    private LruCache<String , Bitmap> mLruCache ;
    private static CacheManager mCacheManager ;

    /**
     * 获取该类的实例
     * @return
     */
    public static CacheManager getInstance(){
        if (mCacheManager == null){
            synchronized (CacheManager.class){
                if (mCacheManager == null){
                    mCacheManager = new CacheManager();
                }
            }
        }
        return mCacheManager;
    }

    public CacheManager(){
        init();
    }

    private void init(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheMemory = maxMemory / 6 ;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();//返回一张图的大小
            }
        };
    }

    /**
     * 从缓存中获取图片
     * @param key
     * @return
     */
    public Bitmap getFromLruCache(String key){
        return mLruCache.get(key);//根据KEY值从缓存中获取图片
    }

    /**
     * 将图片添加到缓存当中
     * @param key
     * @param bitmap
     */
    public void addToLruCache(String key , Bitmap bitmap){
        if (getFromLruCache(key) == null && bitmap !=null){
            mLruCache.put(key , bitmap);//将图片添加到缓存当中
        }
    }
}
