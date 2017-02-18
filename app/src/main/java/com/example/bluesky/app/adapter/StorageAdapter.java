package com.example.bluesky.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.bean.StorageItem;
import com.example.bluesky.app.callback.OnStorageItemClickListener;
import com.example.bluesky.app.callback.OnStorageItemLongClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bluesky on 16/6/17.
 */
public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.MyViewHolder> {

    List<StorageItem> storageItemList;
    Context context;
    OnStorageItemClickListener mOnItemClickListener;
    OnStorageItemLongClickListener mOnItemLongClickListener;

    public StorageAdapter( Context context) {
        this.context = context;
    }

    public void setStorageItemList(List<StorageItem> storageItemList) {
        this.storageItemList = storageItemList;
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        storageItemList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setmOnStorageItemLongClickListener(OnStorageItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setmOnStorageItemClickListener(OnStorageItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gv_detail, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textView.setText(storageItemList.get(position).getDescription());
        Picasso.with(context).load(storageItemList.get(position).getPicUrl())
                .placeholder(R.mipmap.ic_launcher).into(holder.imageView);
        holder.tv_price.setText("¥" + storageItemList.get(position).getPrice());
        holder.tv_origin_price.setText("原¥" + storageItemList.get(position).getOrigin_price());
        holder.tv_sales.setText("销量:" + storageItemList.get(position).getSales());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onStorageItemClickListener(position);
                }

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onStorageItemLongClickListener(position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return storageItemList!=null?storageItemList.size():0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView tv_price;
        TextView tv_origin_price;
        TextView tv_sales;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            tv_price = (TextView) itemView.findViewById(R.id.tv_shopping_cart_price);
            tv_origin_price = (TextView) itemView.findViewById(R.id.tv_origin_price);
            tv_sales = (TextView) itemView.findViewById(R.id.tv_sales);
        }
    }


}
