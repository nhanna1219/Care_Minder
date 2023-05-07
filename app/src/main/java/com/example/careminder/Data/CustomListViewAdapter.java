package com.example.careminder.Data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.careminder.Activity.Food.FoodItemDetailActivity;
import com.example.careminder.Activity.Food.Food;
import com.example.careminder.R;

import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<Food> {
    private int layoutResource;
    private Activity activity;
    private ArrayList<Food> foodList = new ArrayList<>();

    public CustomListViewAdapter(@NonNull Activity activity, int resource, ArrayList<Food> data) {
        super(activity, resource, data);
        layoutResource = resource;
        this.activity = activity;
        foodList = data;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Nullable
    @Override
    public Food getItem(int position) {

        return foodList.get(position);
    }

    @Override
    public int getPosition(@Nullable Food item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        //if row doesn't exist, create it
        if (row == null || (row.getTag() == null)){
            //inflate
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResource, null);
            //make new viewholder
            holder = new ViewHolder();
            holder.foodName = (TextView) row.findViewById(R.id.nameTxt);
            holder.foodCalories = (TextView) row.findViewById(R.id.caloriesTxt);
            holder.foodDate = (TextView) row.findViewById(R.id.dateTxt);

            row.setTag(holder);
        }
        else {
            //row already exists, get current row viewholder
            holder = (ViewHolder) row.getTag();
        }

        //get food item of current row, set TextViews to it's data
        holder.food = getItem(position);
        holder.foodName.setText(holder.food.getName());
        holder.foodDate.setText(holder.food.getRecordDate());
        holder.foodCalories.setText(String.valueOf(holder.food.getCalories()));

        final ViewHolder finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to details activity
                Intent intent = new Intent(activity, FoodItemDetailActivity.class);
                //bundle for food
                Bundle mBundle = new Bundle();
                //put serialized food object bundle
                mBundle.putSerializable("userObj", finalHolder.food);
                intent.putExtras(mBundle);
                //start food item details activity
                activity.startActivity(intent);
            }
        });

        return row;

    }
//ViewHolder for food
    public class ViewHolder {
        Food food;
        TextView foodName, foodCalories, foodDate;
    }
}
