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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.careminder.Activity.Food.Food;
import com.example.careminder.R;

import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<Food> {
    private int layoutResource;
    private Activity activity;
    private ArrayList<Food> foodList;
    private Note db;
    private Food myFood;
    private ArrayList<Food> dbFoods = new ArrayList<>();

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
            holder.Note = (TextView) row.findViewById(R.id.note);
            row.setTag(holder);
        }
        else {
            //row already exists, get current row viewholder
            holder = (ViewHolder) row.getTag();
        }

        //get food item of current row, set TextViews to it's data
        db = new Note(activity);
        //get all foods from DB
        ArrayList<Food> foodsFromDB = db.getAllFoods();
        holder.food = getItem(position);
        holder.food.setId(getItem(position).getId());
        for(int i = 0; i <foodsFromDB.size(); i++){
            int iden = foodsFromDB.get(i).getId();
            if(iden == getItem(position).getId() ){
                holder.Note.setText(holder.food.getNote());
            }
        }
//        holder.foodName.setText(holder.food.getName());
//        holder.foodCalories.setText(holder.food.getCalories().toString());
//        holder.mealType.setText(holder.food.getMealtype());
        final ViewHolder finalHolder = holder;

        AppCompatImageButton btn = row.findViewById(R.id.detailItem);
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
        TextView foodName, foodCalories, mealType, Note;


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
        CaloTextView.setText(food.getCalories().toString());
        MealTypeTextView.setText(food.getMealtype());
        NoteTextView.setText(food.getNote());
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


