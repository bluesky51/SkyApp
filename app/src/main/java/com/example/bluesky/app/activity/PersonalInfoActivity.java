package com.example.bluesky.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.event.NotifyExitEvent;
import com.example.bluesky.app.utils.PerferenceKeyUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;

public class PersonalInfoActivity extends AppCompatActivity {

    @BindView(R.id.btn_exit)
    Button btn_exit;
    @BindView(R.id.loginInfo_toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_info_head)
    SimpleDraweeView img_head;
    @BindView(R.id.tv_info_name)
    TextView tv_name;
    @BindView(R.id.tv_info_gender)
    TextView tv_gender;
    @BindView(R.id.tv_info_hometown)
    TextView tv_hometown;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitle("个人信息");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        sharedPreferences = ShoppingApplication.sp;
        String name = sharedPreferences.getString(PerferenceKeyUtils.USERNAME, "");
        String img = sharedPreferences.getString(PerferenceKeyUtils.USER_HEAD_IMG, "");
        String gender = sharedPreferences.getString(PerferenceKeyUtils.GENDER, "");
        String hometown = sharedPreferences.getString(PerferenceKeyUtils.HOMETOWN, "");
        tv_name.setText(name);
        if (gender.equals("f")){
            tv_gender.setText("女");
        }else{
            tv_gender.setText("男");
        }

        tv_hometown.setText(hometown);
        img_head.setImageURI(img);
    }

    @OnClick(R.id.btn_exit)
    public void exit(View view) {
        String plarform = ShoppingApplication.sp.getString(PerferenceKeyUtils.PLATFORM, "");
        ShareSDK.initSDK(PersonalInfoActivity.this);
        ShareSDK.getPlatform(PersonalInfoActivity.this,plarform).removeAccount();
        EventBus.getDefault().post(new NotifyExitEvent());
        //清除所有保存登录的信息
        ShoppingApplication.sp.edit()
                .putString(PerferenceKeyUtils.PLATFORM, "")
                .putString(PerferenceKeyUtils.USERNAME, "")
                .putString(PerferenceKeyUtils.USER_HEAD_IMG, "")
                .putString(PerferenceKeyUtils.GENDER, "")
                .putBoolean(PerferenceKeyUtils.ISLOGIN, false).commit();
         startActivity(new Intent(PersonalInfoActivity.this, LoginActivity.class));
        finish();
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

}
