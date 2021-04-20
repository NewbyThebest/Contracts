package com.geekb.contacts;

import java.util.ArrayList;
import java.util.List;

public class CommonManager {
    private List<UserInfo> userInfoList = new ArrayList<>();
    private static CommonManager manager;
    private CommonManager() {

    }

    public static CommonManager getInstance() {
        if(manager == null){
            synchronized (CommonManager.class){
                if (manager == null){
                    manager = new CommonManager();
                }
            }
        }
        return manager;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void addUserInfo(UserInfo info){
        userInfoList.add(info);
    }

    public void removeUserInfo(UserInfo info){
        userInfoList.remove(info);
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
