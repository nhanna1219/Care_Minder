package com.example.careminder.utils;
import static androidx.recyclerview.widget.RecyclerView.*;

import com.example.careminder.*;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {
    private List<SettingItem> settingItems;
    private View.OnClickListener clickListener;

    public SettingAdapter(List<SettingItem> settingItems) {
        this.settingItems = settingItems;
    }

    @NonNull
//    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new SettingViewHolder(view);
    }
    public void setOnItemClickListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }

    //    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        SettingItem item = settingItems.get(position);
        holder.titleTextView.setText(item.getTitle());

        ImageView iconImageView = holder.itemView.findViewById(R.id.itemIcon);
        iconImageView.setImageResource(item.getIconResId());


        // Gán listener cho itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Context context = view.getContext();
                switch (position) {
                    // Account Setting Activity
                    case 0:
                        Intent intent = new Intent(context, AccountActivity.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(context, NotifyActivity.class);
                        context.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, AdvancedSettingActivity.class);
                        context.startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, PrivacySettingActivity.class);
                        context.startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(context, LogoutActivity.class);
                        context.startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(context, HelpSettingActivity.class);
                        context.startActivity(intent5);
                        break;

                    // hoạt động khác
                }
            }
        });

    }

    //    @Override
    public int getItemCount() {
        return settingItems.size();
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public SettingViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }


}

