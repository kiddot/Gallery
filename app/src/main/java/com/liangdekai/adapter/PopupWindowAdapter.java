package com.liangdekai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liangdekai.bean.ImageFolder;
import com.liangdekai.photodepot.R;
import com.liangdekai.imageloader.ImageLoader;

import java.util.List;

public class PopupWindowAdapter extends BaseAdapter {
    private Context mContext ;
    private List<ImageFolder> mFolder ;
    //private TaskManager mLoadImage ;
    private ImageLoader mImageLoader;

    public PopupWindowAdapter(Context context, List<ImageFolder> folder) {
        this.mContext = context ;
        this.mFolder = folder ;
        //mLoadImage = TaskManager.getInstance();
        mImageLoader = ImageLoader.getInstance() ;
    }

    @Override
    public int getCount() {
        return mFolder.size();
    }

    @Override
    public Object getItem(int i) {
        return mFolder.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder ;
        View view ;
        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.part_choose_lv_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.choose_iv_image);
            holder.FileName = (TextView) view.findViewById(R.id.choose_tv_name);
            holder.ImageCount = (TextView) view.findViewById(R.id.choose_tv_count);
            view.setTag(holder);
        }else{
            view = convertView ;
            holder = (ViewHolder) view.getTag();
        }
        //mLoadImage.loadLargeImage(mFolder.get(i).getFirstImagePath() , holder.imageView);
        mImageLoader.loadLargeImage(mFolder.get(i).getFirstImagePath() , holder.imageView);
        holder.FileName.setText(mFolder.get(i).getFolderName());
        String folderFile = mFolder.get(i).getFileCount()+"张";
        holder.ImageCount.setText(folderFile);
        return view;
    }

    class ViewHolder{
        ImageView imageView ;
        TextView FileName ;
        TextView ImageCount ;
    }
}
