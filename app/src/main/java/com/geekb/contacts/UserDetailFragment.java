package com.geekb.contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class UserDetailFragment extends Fragment implements View.OnClickListener {
    private EditText etName;
    private EditText etPhone;
    private EditText etMsg;
    private Button delete;
    private Button modify;
    private Button call;
    private Button send;
    private Button back;

    private UserInfo userInfo;


    private static final int SEND_SMS = 100;
    private static final int CALL_PHONE = 101;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userInfo = getArguments().getParcelable("userinfo");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        etName = view.findViewById(R.id.et_user);
        etPhone = view.findViewById(R.id.et_phone);
        etMsg = view.findViewById(R.id.et_msg);
        delete = view.findViewById(R.id.delete);
        modify = view.findViewById(R.id.modify);
        call = view.findViewById(R.id.call);
        send = view.findViewById(R.id.send);
        back = view.findViewById(R.id.back);
        delete.setOnClickListener(this);
        modify.setOnClickListener(this);
        call.setOnClickListener(this);
        send.setOnClickListener(this);
        back.setOnClickListener(this);
        if (userInfo != null) {
            etName.setText(userInfo.name);
            etPhone.setText(userInfo.phone);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_LONG).show();
                CommonManager.getInstance().removeUserInfo(userInfo);
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.modify:
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                    userInfo.name = name;
                    userInfo.phone = phone;
                    Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "姓名或电话不能为空！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.call:
                requestPhonePermission();
                break;
            case R.id.send:
                requestMsgPermission();
                break;
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            default:
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "发送信息失败！", Toast.LENGTH_SHORT).show();
                }
                break;
            case CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "拨打电话失败！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestMsgPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, SEND_SMS);
            } else {
                sendSMS();
            }
        } else {

            sendSMS();
        }
    }

    private void requestPhonePermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE);
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }

    private void callPhone() {
        String phoneNumber = etPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(getActivity(), "电话为空无法拨打电话！", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }


    //发送短信
    private void sendSMS() {
        String content = etMsg.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(phone)) {
            SmsManager manager = SmsManager.getDefault();
            ArrayList<String> strings = manager.divideMessage(content);
            for (int i = 0; i < strings.size(); i++) {
                manager.sendTextMessage(phone, null, content, null, null);
            }
            Toast.makeText(getContext(), "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "手机号或内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
