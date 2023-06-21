package com.example.careminder.Data;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careminder.Activity.Food.Food;
import com.example.careminder.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList note_id, title, note, date;

    public CustomAdapter(Activity activity, Context context, ArrayList note_id, ArrayList title ,ArrayList note, ArrayList date){
        this.activity = activity;
        this.context = context;
        this.note_id = note_id;
        this.note = note;
        this.title = title;
        this.date = date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_note, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.note_date_txt.setText(String.valueOf(date.get(position)));
        holder.titletxt.setText(String.valueOf(title.get(position)));
        holder.note = String.valueOf(note.get(position));
        holder.id = String.valueOf(note_id.get(position));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDetailDialog(holder.titletxt.getText().toString(), holder.note, holder.id);
            }
        });


    }

    @Override
    public int getItemCount() {
        return note_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView note_date_txt, titletxt;
        String note, id;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note_date_txt = itemView.findViewById(R.id.dateNote);
            titletxt = itemView.findViewById(R.id.titlenote_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

    private void showNoteDetailDialog(String title, String note, String id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.detail_note, null);
        dialogBuilder.setView(dialogView);
        // Set the Note details
        TextView Title = dialogView.findViewById(R.id.detTitleTxt);
        TextView Note = dialogView.findViewById(R.id.textViewContent);

        Title.setText(title);
        Note.setText(note);

        // Delete button click listener
        Button deleteButton = dialogView.findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete logic here
//                deleteFood(food);
                confirmDialog(id, title);

            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }




    void confirmDialog(String id, String titleValue){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete " + titleValue + " ?");
        builder.setMessage("Are you sure you want to delete this ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(activity);
                myDB.deleteOneRow(id);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


}
