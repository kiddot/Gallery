package com.liangdekai.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.liangdekai.bean.ImageFolder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 扫描手机中所有的图片
 */
public class ScanFile {

    /**
     * 接口回调
     */
    public interface ScanListener{
        void succeed(List<String> imageList , List<ImageFolder> folderList);
        void failed();
    }

    /**
     * 扫描手机文件中的图片
     * @param context
     * @param listener
     */
    public static void scanImageFile(final Context context , final ScanListener listener){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            listener.failed();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ImageFolder> folderList = new ArrayList<ImageFolder>();
                List<String> imageList = new ArrayList<String>();//装载所有图片的路径
                List<File> fileList  = new ArrayList<File>();//装载扫描过的文件夹路径，避免重复获取
                ImageFolder folder = new ImageFolder();
                folder.setFolderName("所有图片");
                folderList.add(folder);
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI ;
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(uri , null , MediaStore.Images.Media.MIME_TYPE +"=? or "+ MediaStore.Images.Media.MIME_TYPE+"=? or "+ MediaStore.Images.Media.MIME_TYPE+"=?" ,
                        new String[]{"image/jpg","image/png" , "image/jpeg"} , MediaStore.Images.Media.DATE_MODIFIED );
                while (cursor.moveToNext()){
                    List<String> temp ;
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File imageFile = new File(path).getParentFile();
                    temp = Arrays.asList(imageFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            return s.endsWith(".jpg")||s.endsWith(".png") || s.endsWith(".jpeg");
                        }
                    }));
                    if (fileList.contains(imageFile)){
                        continue;
                    }
                    //获取所有包含图片的文件
                    ImageFolder imageFolder = new ImageFolder();
                    imageFolder.setFolderDir(imageFile.getAbsolutePath());
                    imageFolder.setFileCount(temp.size());
                    imageFolder.setFirstImagePath(imageFile.getAbsolutePath()+"/"+temp.get(0));
                    imageFolder.setFolderName(imageFile.getAbsolutePath());
                    folderList.add(imageFolder);
                    //将扫描过的文件装入容器中
                    fileList.add(imageFile);
                    for (int i = 0 ; i < temp.size() ; i++){
                        String absolutePath = imageFile.getAbsolutePath()+"/"+temp.get(i);
                        imageList.add(absolutePath);
                    }
                }
                listener.succeed(imageList , folderList);
                cursor.close();
            }
        }).start();
    }
}
