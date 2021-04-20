package com.geekb.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String jsonStr = MMKV.defaultMMKV().decodeString("userinfolist");
        if (!TextUtils.isEmpty(jsonStr)) {
            List<UserInfo> userInfoList = CommonManager.getInstance().getUserInfoList();
            userInfoList.clear();
            try {
                Gson gson = new Gson();
                JsonArray array = JsonParser.parseString(jsonStr).getAsJsonArray();
                for (JsonElement jsonElement : array) {
                    userInfoList.add(gson.fromJson(jsonElement, UserInfo.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_container, mainFragment, "main")
                .commitAllowingStateLoss();
    }


    public void jumpToAddPage() {
        AddUserFragment addUserFragment = (AddUserFragment) getSupportFragmentManager().findFragmentByTag("add");
        if (addUserFragment == null) {
            addUserFragment = new AddUserFragment();
        }
        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("main");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().hide(fragment)
                    .add(R.id.layout_container, addUserFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_container, addUserFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    public void jumpToAllPage() {
        AllUserFragment allUserFragment = (AllUserFragment) getSupportFragmentManager().findFragmentByTag("all");
        if (allUserFragment == null) {
            allUserFragment = new AllUserFragment();
        }
        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("main");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().hide(fragment)
                    .add(R.id.layout_container, allUserFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_container, allUserFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    public void jumpToDetailPage(UserInfo userInfo) {
        UserDetailFragment userDetailFragment = (UserDetailFragment) getSupportFragmentManager().findFragmentByTag("detail");
        if (userDetailFragment == null) {
            userDetailFragment = new UserDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("userinfo",userInfo);
            userDetailFragment.setArguments(bundle);
        }
        AllUserFragment fragment = (AllUserFragment) getSupportFragmentManager().findFragmentByTag("all");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().hide(fragment)
                    .add(R.id.layout_container, userDetailFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_container, userDetailFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Gson gson = new Gson();
        String strJson = gson.toJson(CommonManager.getInstance().getUserInfoList());
        MMKV.defaultMMKV().encode("userinfolist", strJson);
    }

    // Activity中
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null)
        {
            return;
        }
        // 查找在Fragment中onRequestPermissionsResult方法并调用
        for (Fragment fragment : fragments)
        {
            if (fragment != null)
            {
                // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}