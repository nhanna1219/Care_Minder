package com.example.careminder.utils;
import static androidx.recyclerview.widget.RecyclerView.*;

import com.example.careminder.*;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {
    private List<SettingItem> settingItems;

    public SettingAdapter(List<SettingItem> settingItems) {
        this.settingItems = settingItems;
    }

    @NonNull
//    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new SettingViewHolder(view);
    }

    //    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        SettingItem item = settingItems.get(position);
        holder.titleTextView.setText(item.getTitle());
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

