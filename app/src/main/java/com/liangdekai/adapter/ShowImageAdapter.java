package com.liangdekai.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.liangdekai.photodepot.R;
import com.liangdekai.imageloader.task.internel.CompressTask;

import java.util.List;


public class ShowImageAdapter extends BaseAdapter{
    private List<String> mImageList ;
    private Context mContext ;

    public ShowImageAdapter(Context context , List<String> list){
        this.mContext = context ;
        this.mImageList = list ;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder ;
        View view ;
        if (convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gv_show_image , viewGroup , false);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.choose_image_iv_item);
            viewHolder.imageView.setTag(mImageList.get(i));
            view.setTag(viewHolder);
        }else {
            view = convertView ;
            viewHolder = (ViewHolder) view.getTag();
        }
        Bitmap bitmap = CompressTask.compressImage(mImageList.get(i) , 100 , 100);
        viewHolder.imageView.setImageBitmap(bitmap);
        return view;
    }

    class ViewHolder{
        ImageView imageView ;
    }
}
