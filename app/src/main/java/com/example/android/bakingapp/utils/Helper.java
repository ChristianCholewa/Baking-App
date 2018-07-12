package com.example.android.bakingapp.utils;

import android.content.Context;

import com.example.android.bakingapp.R;

public class Helper {

    // build title for activity.
    // a) <recipe name> - Ingredients
    // b) <recipe name> - Step x
    public static String getTitle(Context context, String recipeName, int selectedStep){
        String title = recipeName + " - ";
        if (selectedStep == 0) {
            title += context.getString(R.string.detail_ingredient_titel);
        } else if (selectedStep == 1) {
            // step "1" is the "Recipe Introduction" and is step "0"
            title += context.getString(R.string.detail_introduction);
        } else {
            title += context.getString(R.string.detail_step) + " " + Integer.toString(selectedStep - 1);
        }

        return title;
    }
}
