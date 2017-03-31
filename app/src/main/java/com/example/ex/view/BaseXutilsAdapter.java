package com.example.ex.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 *
 */
@SuppressWarnings("all")
public abstract class BaseXutilsAdapter<T> extends BaseAdapter {
	List<T> dataList = new ArrayList<T>();
    public LayoutInflater mInflater;
    public Context mContext;


    public BaseXutilsAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
 
    /**
     * 设置数据，以前数据会清空 <功能详细描述>
     * 
     * @param data
     * @see 
     */
    public void setData(List<T> data) {
        if (dataList == null)
            dataList = new ArrayList<T>();
        dataList.clear();
        if (data != null)
            dataList.addAll(data);
        notifyDataSetChanged();
    }
 
    /**
     * 在原始数据上添加新数据 <功能详细描述>
     * 
     * @param data
     * @see 
     */
    public void addData(List<T> data) {
        if (dataList == null)
            dataList = new ArrayList<T>();
        dataList.addAll(data);
        notifyDataSetChanged();
    }
 
    public void removeAllData() {
        dataList.clear();
        notifyDataSetChanged();
    }
    public void removeAllData(boolean b) {
        dataList.clear();
        if (b) {
            notifyDataSetChanged();
        }
    }
 
    @Override
    public int getCount() {
        if (dataList != null)
            return dataList.size();
        return 0;
    }
 
    @Override
    public T getItem(int position) {
        if (dataList != null)
            try {
                return dataList.get(position);
            } catch (Exception g) {
                return null;
            }
        return null;
    }
 
    @Override
    public long getItemId(int position) {

        return position;
    }
 
    public abstract int getConvertViewId(int layoutId);
 
    public abstract BaseViewHolderInject<T> getNewHolder(int position);
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	BaseViewHolderInject<T> holder;
        if (convertView == null) {
            convertView = mInflater.inflate(getConvertViewId(position), null);
            holder = getNewHolder(position);
            ButterKnife.bind(holder, convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
//            AutoUtils.autoSize(convertView);
        } else {
            holder = (BaseViewHolderInject<T>) convertView.getTag();
        }
        holder.loadData(getItem(position), position);
        return convertView;
    }



    public abstract class BaseViewHolderInject<T> {
        /**
         * 装载数据 <功能详细描述>
         *
         * @param data
         * @see [类、类#方法、类#成员]
         */
        public abstract void loadData(T data, int position);
    }

}

