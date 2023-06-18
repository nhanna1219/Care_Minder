package com.example.careminder.Data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

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


            row.setTag(holder);
        }
        else {
            //row already exists, get current row viewholder
            holder = (ViewHolder) row.getTag();
        }

        //get food item of current row, set TextViews to it's data
        holder.food = getItem(position);
        holder.foodName.setText(holder.food.getName());

        final ViewHolder finalHolder = holder;

        Button btn = row.findViewById(R.id.detailItem);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodDetailDialog(finalHolder.food);
            }
        });

        return row;

    }
//ViewHolder for food
    public class ViewHolder {
        Food food;
        TextView foodName, foodCalories, mealType;
    }
    ////DIALOG
    private void showFoodDetailDialog(Food food) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_food_detail, null);
        dialogBuilder.setView(dialogView);

        // Set the food details
        TextView foodNameTextView = dialogView.findViewById(R.id.detFoodTxt);
        TextView CaloTextView = dialogView.findViewById(R.id.detCaloriesValueTxt);
        TextView MealTypeTextView = dialogView.findViewById(R.id.detMealTypeTxt);
        TextView NoteTextView = dialogView.findViewById(R.id.NoteTxt);

        foodNameTextView.setText(food.getName());
//        NoteTextView.setText(food.getDescription());

        // Delete button click listener
        Button deleteButton = dialogView.findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete logic here
//                deleteFood(food);
//                dialog.dismiss();
            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }



}


