package com.liangdekai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.liangdekai.activity.ImageDetailActivity;
import com.liangdekai.photodepot.R;
import com.liangdekai.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ChooseImageAdapter extends BaseAdapter{
    private List<String> mSelectedImage;
    private List<String> mImageList ;
    //private TaskManager mLoadImage ;
    private ImageLoader mImageLoader;
    private onCountChangeListener onChangeListener ;

    public ChooseImageAdapter(List<String> list){
        this.mImageList = list ;
        //mLoadImage = TaskManager.getInstance();
        mImageLoader = ImageLoader.getInstance() ;
        mSelectedImage = new ArrayList<String>();
    }


    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int i) {
        return mImageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder ;
        View view ;
        if (convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gv_choose_image, viewGroup , false);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.choose_image_iv_item);
            viewHolder.imageButton = (ImageButton) view.findViewById(R.id.choose_ib_select);
            view.setTag(viewHolder);
        }else {
            view = convertView ;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageView.setImageResource(R.mipmap.empty);
        viewHolder.imageView.setTag(mImageList.get(i));//为每个图片控件设置TAG
        viewHolder.imageButton.setTag(mImageList.get(i));

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnPhotoClickListener!=null)
                mOnPhotoClickListener.onClick(mImageList,i);
            }
        });
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(mImageList.get(i) , viewHolder);//点击勾选按钮的具体事件
            }
        });

        mImageLoader.loadImage(mImageList.get(i) , viewHolder.imageView);
        return view;
    }
    private OnPhotoClickListener mOnPhotoClickListener;

    public void setOnPhotoClickListener(OnPhotoClickListener listener ){
        mOnPhotoClickListener =listener;
    }
    public interface OnPhotoClickListener{
        void onClick(List<String> path,int currentPosition);
    }

    /**
     * 按钮具体的点击操作逻辑
     * @param path
     * @param viewHolder
     */
   private void onButtonClick(String path , ViewHolder viewHolder){
        if (mSelectedImage.contains(path)){//如果已选容器中存在点击对应的图片路径，则视为取消选择
            mSelectedImage.remove(path);//从容器中移除该路径
            viewHolder.imageButton.setImageResource(R.mipmap.picture_unselected);//改变按钮状态为未选状态
            viewHolder.imageView.setColorFilter(null);//取消滤镜
            onChangeListener.onChange(mSelectedImage.size());
        }else{
            mSelectedImage.add(path);//添加已经选择的路径到容器中
            viewHolder.imageButton.setImageResource(R.mipmap.pictures_selected);//改变按钮状态为已选状态
            viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));//设置滤镜，点击改变颜色
            onChangeListener.onChange(mSelectedImage.size());
        }
    }

    /**
     * 返回已选容器
     * @return
     */
    public List<String> getSelectedList(){
        return mSelectedImage;
    }

    public void setOnChangeListener(onCountChangeListener onChangeListener){
        this.onChangeListener = onChangeListener ;
    }

    public interface onCountChangeListener{
        void onChange(int count);
    }

    class ViewHolder {
        ImageView imageView ;
        ImageButton imageButton ;
    }
}
