package com.example.bluesky.app.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.activity.ItemListDetailActivity;
import com.example.bluesky.app.adapter.StorageAdapter;
import com.example.bluesky.app.bean.StorageItem;
import com.example.bluesky.app.callback.OnStorageItemClickListener;
import com.example.bluesky.app.callback.OnStorageItemLongClickListener;
import com.example.bluesky.app.utils.PerferenceKeyUtils;

import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StorageFragment extends Fragment implements OnStorageItemClickListener, OnStorageItemLongClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<StorageItem> list;
    StorageAdapter adapter;
    @BindView(R.id.tv)
    TextView tv_tip;
    MyReceiver myReceiver;

    public StorageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_storage, container, false);
        ButterKnife.bind(this, view);
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("login");
        getActivity().registerReceiver(myReceiver, filter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new StorageAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        boolean isLogin = ShoppingApplication.sp.getBoolean(PerferenceKeyUtils.ISLOGIN, false);
        if (isLogin == false) {
            tv_tip.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "请先登录在查看收藏数据", Toast.LENGTH_LONG).show();
        } else {
            tv_tip.setVisibility(View.GONE);
            loadData();
        }

        if (adapter != null) {
            adapter.setmOnStorageItemClickListener(this);
            adapter.setmOnStorageItemLongClickListener(this);
        }
        return view;
    }

    public void loadData() {
        try {
            list = ShoppingApplication.dbManager.findAll(StorageItem.class);
            adapter.setStorageItemList(list);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStorageItemClickListener(int pos) {
        Intent intent = new Intent(getActivity(), ItemListDetailActivity.class);
        StorageItem storageItem = list.get(pos);
        intent.putExtra("type", "1");
        intent.putExtra("storageItem", storageItem);
        startActivity(intent);
    }

    @Override
    public void onStorageItemLongClickListener(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("提示信息");
        builder.setMessage("是否删除该条信息");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ShoppingApplication.dbManager.deleteById(StorageItem.class, list.get(pos).getId());
                    adapter.remove(pos);
                } catch (DbException e) {
                    e.printStackTrace();
                }


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("login")) {
                //登录成功的事件
                tv_tip.setVisibility(View.GONE);
                loadData();
            }

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(myReceiver);
    }
}
