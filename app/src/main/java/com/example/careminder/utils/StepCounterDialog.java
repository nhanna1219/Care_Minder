package com.example.careminder.utils;
import android.app.AlertDialog;
import android.content.Context;

import com.example.careminder.R;

public class StepCounterDialog {
    public static void showInstructionsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("\nStep Counter Instructions");
        builder.setMessage("\nTo use the step counter, follow these steps:\n\n\n"
                + "* Tap the circle to start counting steps.\n\n"
                + "* Tap again to pause counting.\n\n"
                + "* Long tap to reset the steps.\n\n");
        builder.setIcon(R.drawable.app_icon);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
