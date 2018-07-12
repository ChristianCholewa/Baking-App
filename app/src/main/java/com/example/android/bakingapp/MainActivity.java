package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utils.JSonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        RecipesAdapter.ItemClickListener,
        LoaderManager.LoaderCallbacks<String>{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MAIN_LOADER_ID = 1;

    @BindView(R.id.rv_recipes) RecyclerView recyclerView;
    @BindView(R.id.pb_loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.tv_error_message_display) TextView errorMessage;

    private RecipesAdapter recipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int minGridWidthDp = getResources().getInteger(R.integer.gridview_minwidth_dp);
        int minCardWidthDp = getResources().getInteger(R.integer.cardview_minwidth_dp);

        if(dpWidth >= minGridWidthDp) {
            int spanCount = (int)(dpWidth / minCardWidthDp);
            recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        // initialize the loader
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.LOADER_BUNDLE_URL), getString(R.string.source_url));

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Object> dataLoader = loaderManager.getLoader(MAIN_LOADER_ID);

        if (dataLoader == null) {
            loaderManager.initLoader(MAIN_LOADER_ID, bundle, MainActivity.this);
        } else {
            loaderManager.restartLoader(MAIN_LOADER_ID, bundle, MainActivity.this);
        }
    }

    // Access to JSON
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(bundle == null){
                    return;
                }

                loadingIndicator.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                errorMessage.setVisibility(View.INVISIBLE);

                forceLoad();
            }

            @Override
            public String loadInBackground() {

                String jsonString = "";

                try {
                    URL url = new URL(bundle.getString(getString(R.string.LOADER_BUNDLE_URL)));
                    jsonString = NetworkUtils.getResponseFromHttpUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

                return jsonString;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        loadingIndicator.setVisibility(View.INVISIBLE);

        if(!TextUtils.isEmpty(data)) {
            List<Recipe> recipeList = JSonUtils.ParseJson(data);
            recipesAdapter = new RecipesAdapter(MainActivity.this, recipeList);
            recipesAdapter.setClickListener(MainActivity.this);
            recyclerView.setAdapter(recipesAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            errorMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        //nothing to do
    }

    // item click handling, open recipe
    @Override
    public void onItemClick(View view, int position) {
        Recipe recipe = recipesAdapter.getItem(position);
        Log.i(LOG_TAG, String.format("Clicked on %s", recipe.getName()));

        //send ingredients to widget
        Intent widgetIntent = new Intent(this, BakingAppWidgetProvider.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetIntent.putExtra(Recipe.EXTRA_RECIPE_DATA, recipe);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        if(ids != null && ids.length > 0) {
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            this.sendBroadcast(widgetIntent);
        }
        // start detail activity
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Recipe.EXTRA_RECIPE_DATA, recipe);
        startActivity(intent);
    }
}
