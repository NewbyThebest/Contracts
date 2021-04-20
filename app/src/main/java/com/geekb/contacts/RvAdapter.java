package com.geekb.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ItemHolder> {
    private Context mContext;
    private List<UserInfo> mList;
    private ItemClickListener mItemClickListener;

    public RvAdapter(Context context, List<UserInfo> list) {
        mContext = context;
        mList = list;
    }

    public interface ItemClickListener {
        void onItemClick(UserInfo userInfo);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false);

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        UserInfo data = mList.get(position);
        holder.name.setText(data.name);
        holder.phone.setText(data.phone);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClick(data);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView phone;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }

    }
}