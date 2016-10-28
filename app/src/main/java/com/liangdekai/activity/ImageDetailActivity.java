package com.liangdekai.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.liangdekai.photodepot.R;
import com.liangdekai.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ImageDetailActivity extends Activity implements ViewPager.OnPageChangeListener{
    private int mLevel ; //标志当前图片的位置
    private TextView mTextView ;
    private ImageLoader mImageLoader;
    private List<String> mImageList ;
    private SparseArray<ImageView> mViewArray ;

    /**
     * 启动本活动的带参方法
     * @param context
     * @param list
     * @param position
     */
    public static void startActivity(Context context ,ArrayList<String> list , int position){
        Intent intent = new Intent(context , ImageDetailActivity.class);
        intent.putStringArrayListExtra("imageList" , list);
        intent.putExtra("level" , position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_image);
        init();
    }

    /**
     * 初始化控件，并设置viewPager数据源
     */
    private void init(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_vp_detail);
        mTextView = (TextView) findViewById(R.id.activity_tv_record);
        mImageLoader = ImageLoader.getInstance() ;
        mViewArray = new SparseArray<ImageView>() ;
        mImageList = getIntent().getStringArrayListExtra("imageList");//获取所有图片的路径
        mLevel = getIntent().getIntExtra("level", 0);//获取点击图片的当前位置
        setIndex(mLevel);//设置当前图片位置，以及图片总数
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mLevel);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setIndex(position);//设置当前图片位置，以及图片总数
        mImageLoader.loadImageDetail(mImageList.get(position) , mViewArray.get(position));

        /*if (mImageList.size()>position+1 && position-1 >=0){
            mImageLoader.loadLargeImage(mImageList.get(position-1) , mViewArray.get(position-1));
            mImageLoader.loadLargeImage(mImageList.get(position+1) , mViewArray.get(position+1));
        }*/
    }

    /**
     * //设置当前图片位置，以及图片总数
     * @param position
     */
    private void setIndex(int position){
        int temp = position + 1 ;
        String current = ""+temp;
        String sum = ""+mImageList.size();
        String result = current+"/"+sum;
        mTextView.setText(result);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ViewPagerAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ImageDetailActivity.this).inflate(R.layout.part_detail_vp_show, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.detail_iv_show);
            imageView.setTag(mImageList.get(position));
            mViewArray.put(position , imageView);
            mImageLoader.loadLargeImage(mImageList.get(position) , imageView);
            if (position == mLevel){
                mImageLoader.loadImageDetail(mImageList.get(position) , imageView);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
