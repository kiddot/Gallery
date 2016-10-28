package com.liangdekai.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;

import com.liangdekai.adapter.ShowImageAdapter;
import com.liangdekai.photodepot.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity{
    private static final int RESULT_OK = 1;
    private List<String> mSelectedImage ;
    private ImageButton mBtnAdd ;
    private GridView mGridView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        setClickListener();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        mBtnAdd = (ImageButton) findViewById(R.id.activity_main_add);
        mGridView = (GridView) findViewById(R.id.main_gv_photo);
        mSelectedImage = new ArrayList<String>();
    }

    /**
     * 设置事件监听
     */
    private void setClickListener(){
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChooseImageActivity.class);
                startActivityForResult(intent , 1);//启动活动获取数据
            }
        });
    }

    /**
     * 设置GridView数据源
     */
    private void setImage(){
        ShowImageAdapter imageAdapter = new ShowImageAdapter(MainActivity.this, mSelectedImage);
        mGridView.setAdapter(imageAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    mSelectedImage = data.getStringArrayListExtra("selected");
                    setImage();
                }
        }
    }
}
