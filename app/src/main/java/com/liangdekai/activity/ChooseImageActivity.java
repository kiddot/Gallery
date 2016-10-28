package com.liangdekai.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.liangdekai.adapter.ChooseImageAdapter;
import com.liangdekai.adapter.PopupWindowAdapter;
import com.liangdekai.bean.ImageFolder;
import com.liangdekai.photodepot.R;
import com.liangdekai.util.ScanFile;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseImageActivity extends Activity implements View.OnClickListener , ChooseImageAdapter.onCountChangeListener  , AdapterView.OnItemClickListener{
    private static final int RESULT_OK = 1;
    private static final int FAILED = 0;
    private static final int SUCCEED = 1;
    private Button mBtnBack ;
    private Button mBtnFinish ;
    private Button mBtnSkim ;
    private Button mBtnAllImage ;
    private GridView mGridView ;
    private ChooseImageAdapter mAdapter;
    private ProgressDialog progressDialog ;
    private List<String> mImageList;//存储图片路径
    private List<ImageFolder> mFolderList ;//存储包含图片的所有文件夹
    private PopupWindow mPopupWindow ;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCEED :
                    closeDialog();
                    mAdapter = new ChooseImageAdapter( mImageList);
                    mAdapter.setOnPhotoClickListener(new ChooseImageAdapter.OnPhotoClickListener() {
                        @Override
                        public void onClick(List<String> path, int currentPosition) {
                            ImageDetailActivity.startActivity(ChooseImageActivity.this , (ArrayList<String>) mImageList, currentPosition);//启动活动加载大图
                        }
                    });
                    mAdapter.setOnChangeListener(ChooseImageActivity.this);
                    mGridView.setAdapter(mAdapter);
                    break;
                case FAILED :
                    closeDialog();
                    Toast.makeText(ChooseImageActivity.this , "无外部存储" , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_image);
        initView();
        getImage();//扫描手机上的图片，将图片的路径数据回调
        setClickListener();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        mFolderList = new ArrayList<ImageFolder>() ;
        mImageList = new ArrayList<String>() ;
        mGridView = (GridView) findViewById(R.id.main_gv_photo) ;
        mBtnBack = (Button) findViewById(R.id.main_bt_back) ;
        mBtnFinish = (Button) findViewById(R.id.main_bt_finish) ;
        mBtnSkim = (Button) findViewById(R.id.main_bt_skim) ;
        mBtnAllImage = (Button) findViewById(R.id.main_bt_all) ;
    }

    /**
     * 扫描手机上的图片，将图片的路径数据回调
     */
    private void getImage(){
        showDialog();
        ScanFile.scanImageFile(this, new ScanFile.ScanListener() {
            @Override
            public void succeed(List<String> imageList , List<ImageFolder> folderList) {
                ImageFolder imageFolder = folderList.get(0);
                imageFolder.setFileCount(imageList.size());
                imageFolder.setFirstImagePath(imageList.get(0));
                mFolderList = folderList ;
                mImageList = imageList ;
                Message message = Message.obtain() ;
                message.what = SUCCEED ;
                mHandler.sendMessage(message) ;
            }

            @Override
            public void failed() {
                Message message = Message.obtain();
                message.what = FAILED ;
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 设置事件监听
     */
    private void setClickListener(){
        mBtnBack.setOnClickListener(this);
        mBtnFinish.setOnClickListener(this);
        mBtnSkim.setOnClickListener(this);
        mBtnAllImage.setOnClickListener(this);
    }

    /**
     *创建展示对话框
     */
    private void showDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("请耐心等待");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_bt_back :
                finish();
                break;
            case R.id.main_bt_finish :
                Intent intent = new Intent();
                intent.putStringArrayListExtra("selected" , (ArrayList<String>) mAdapter.getSelectedList());
                setResult(RESULT_OK, intent);//返回选择的图片路径数据到上一活动
                finish();
                break;
            case R.id.main_bt_skim :
                List<String> selectedList = mAdapter.getSelectedList();
                if (selectedList.size() > 0 ){
                    ImageDetailActivity.startActivity(ChooseImageActivity.this , (ArrayList<String>) selectedList, 0);
                }else {
                    Toast.makeText(ChooseImageActivity.this , "请勾选预览图" , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.main_bt_all :
                initPopupWindow();//初始化
                mPopupWindow.showAtLocation(mBtnAllImage , Gravity.BOTTOM, 0 , 150);//展现Window
                break;
        }
    }

    private void initPopupWindow(){
        View view = LayoutInflater.from(ChooseImageActivity.this).inflate(R.layout.activity_choose_popup_window , null);
        ListView listView = (ListView) view.findViewById(R.id.choose_lv_popup);
        PopupWindowAdapter popWindowAdapter = new PopupWindowAdapter(ChooseImageActivity.this, mFolderList);
        listView.setAdapter(popWindowAdapter);
        listView.setOnItemClickListener(this);
        int width = getResources().getDisplayMetrics().widthPixels ;
        int height = getResources().getDisplayMetrics().heightPixels - 350;
        mPopupWindow = new PopupWindow(view ,
                width , height , true);
        mPopupWindow.setAnimationStyle(R.style.contextMenuAnim);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    /**
     * 选择图片时数量的改变
     * @param count
     */
    @Override
    public void onChange(int count) {
        mBtnSkim.setText("预览(" + count + ")");
        mBtnFinish.setText("完成(" + count + ")");
    }

    /**
     * PopupWindow项点击事件
     * @param adapterView
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position != 0){
            List<String> temp ;
            String folderDir =  mFolderList.get(position).getFolderDir();
            File imageFile = new File(folderDir);
            temp = Arrays.asList(imageFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.endsWith(".jpg")||s.endsWith(".png") || s.endsWith(".jpeg");
                }
            }));
            mImageList.clear();
            for (int i= 0 ; i < temp.size() ; i++){//拼装图片地址并添加到容器中
                String path = folderDir+"/"+temp.get(i);
                mImageList.add(path);
            }
            mAdapter.notifyDataSetChanged();
            mPopupWindow.dismiss();
        }else{
            //Log.d("test" , getTempList().get(0)+"111");
            /*mImageList = getTempList() ;
            mAdapter.notifyDataSetChanged();
            mPopupWindow.dismiss();*/
        }
    }
}
