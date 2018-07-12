package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static private Recipe recipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);

        CharSequence widgetTitleText;
        CharSequence widgetIngredientsText;

        if(recipe != null){
            StringBuilder builder = new StringBuilder();

            for(Ingredient ingredient : recipe.getIngredientList()){
                builder.append(ingredient.getIngredient());
                builder.append(", ");
                builder.append(Double.toString(ingredient.getQuantity()));
                builder.append(", ");
                builder.append(ingredient.getMeasure());
                builder.append("\n");
            }
            widgetTitleText = recipe.getName();
            widgetIngredientsText = builder.toString();
            views.setViewVisibility(R.id.appwidget_image, View.INVISIBLE);
        } else {
            widgetTitleText = context.getString(R.string.appwidget_text);
            widgetIngredientsText = "";
            views.setViewVisibility(R.id.appwidget_image, View.VISIBLE);
        }

        views.setTextViewText(R.id.appwidget_title, widgetTitleText);
        views.setTextViewText(R.id.appwidget_ingredients, widgetIngredientsText);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widged_layoutFrame, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.hasExtra(Recipe.EXTRA_RECIPE_DATA)) {
            recipe = intent.getParcelableExtra(Recipe.EXTRA_RECIPE_DATA);
        } else {
            recipe = null;
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

