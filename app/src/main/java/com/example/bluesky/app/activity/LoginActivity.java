package com.example.bluesky.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.utils.PerferenceKeyUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_qq)
    ImageView img_QQ;
    @BindView(R.id.img_wechat)
    ImageView img_Wechat;
    @BindView(R.id.img_sina)
    ImageView img_Sina;
    @BindView(R.id.img_tencent)
    ImageView img_Tencent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        toolbar.setTitle("登录");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.img_qq)
    public void LogindQQ(View view) {
        login(QQ.NAME);
    }

    @OnClick(R.id.img_wechat)
    public void LogindWechat(View view) {
        login(Wechat.NAME);
    }

    @OnClick(R.id.img_sina)
    public void LogindSina(View view) {
        login(SinaWeibo.NAME);
    }

    @OnClick(R.id.img_tencent)
    public void LogindTencent(View view) {
        login(TencentWeibo.NAME);
    }


    public void login(String name) {
       Platform platform = ShareSDK.getPlatform(this,name);
      platform.setPlatformActionListener(new PlatformActionListener() {
          @Override
          public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
              ShoppingApplication.sp.edit()
                      .putString(PerferenceKeyUtils.PLATFORM, platform.getName())
                      .putString(PerferenceKeyUtils.USERNAME, platform.getDb().getUserName())
                      .putString(PerferenceKeyUtils.USER_HEAD_IMG, platform.getDb().getUserIcon())
                      .putString(PerferenceKeyUtils.GENDER,platform.getDb().getUserGender())
                      .putBoolean(PerferenceKeyUtils.ISLOGIN, true).commit();
              Intent intent=new Intent("login");
              sendBroadcast(intent);
          finish();
          }

          @Override
          public void onError(Platform platform, int i, Throwable throwable) {

          }

          @Override
          public void onCancel(Platform platform, int i) {

          }
      });
        platform.showUser(null);

    }


}
