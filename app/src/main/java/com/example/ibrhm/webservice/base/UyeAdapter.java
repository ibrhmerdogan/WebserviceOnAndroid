package com.example.ibrhm.webservice.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ibrhm.webservice.R;

import java.util.ArrayList;

/**
 * Created by ibrhm on 7.02.2017.
 */

public class UyeAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Uye> mList;
    private LayoutInflater mLayoutInflater = null;
    public UyeAdapter(Context context, ArrayList<Uye> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        UyeViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.uye_base_layout, null);
            viewHolder = new UyeViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (UyeViewHolder) v.getTag();
        }
        viewHolder.etAdSoyad.setText(mList.get(position).getAdSoyad());
        viewHolder.etEmail.setText(mList.get(position).getEmail());
        return v;
    }
}
class UyeViewHolder {
    public TextView etAdSoyad;
    public TextView etEmail;
    public UyeViewHolder(View base) {
        etAdSoyad = (TextView) base.findViewById(R.id.textView6);
        etEmail = (TextView) base.findViewById(R.id.textView8);
    }
}