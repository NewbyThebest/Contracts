package com.geekb.contacts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllUserFragment extends Fragment {
    private RecyclerView recyclerView;
    private RvAdapter rvAdapter;
    private List<UserInfo> userInfoList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all,container,false);
        recyclerView = view.findViewById(R.id.rv);
        userInfoList = CommonManager.getInstance().getUserInfoList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        rvAdapter = new RvAdapter(getContext(),userInfoList);
        recyclerView.setAdapter(rvAdapter);

        rvAdapter.setOnItemClickListener(new RvAdapter.ItemClickListener() {
            @Override
            public void onItemClick(UserInfo userInfo) {
                ((MainActivity)getActivity()).jumpToDetailPage(userInfo);
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
