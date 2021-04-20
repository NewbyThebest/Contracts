package com.geekb.contacts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddUserFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        EditText etName = view.findViewById(R.id.et_user);
        EditText etPhone = view.findViewById(R.id.et_phone);
        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                    CommonManager.getInstance().addUserInfo(new UserInfo(name, phone));
                    etName.setText("");
                    etPhone.setText("");
                    Toast.makeText(getActivity(),"添加成功！",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(),"姓名或电话不能为空！",Toast.LENGTH_LONG).show();
                }
            }
        });
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

}
