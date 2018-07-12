package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;
import com.example.android.bakingapp.utils.Helper;

public class DetailActivity extends AppCompatActivity implements StepListFragment.OnStepItemClickListener{

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private Recipe recipe = null;

    boolean tabletMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //this view does only exist in activity_detail(sw600dp)
        tabletMode = this.findViewById(R.id.seperator) != null;

        // make sure extras are set
        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
            finish();
        } else if (!intent.hasExtra(Recipe.EXTRA_RECIPE_DATA)) {
            Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            recipe = intent.getParcelableExtra(Recipe.EXTRA_RECIPE_DATA);
        }

        //don't create anything when only rotated
        if(savedInstanceState == null) {
            Log.i(LOG_TAG, "Creating new fragment");

            StepListFragment stepListFragment = new StepListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Recipe.EXTRA_RECIPE_DATA, recipe);
            stepListFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.steplist_container, stepListFragment)
                    .commit();

            String title = recipe.getName();
            if(tabletMode) {
                Fragment stepFragment = new IngredientsFragment();
                stepFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .add(R.id.selected_step_container, stepFragment)
                        .commit();

                title += " - " + getString(R.string.detail_ingredient_titel);
            }

            setTitle(title);
        }
    }

    @Override
    public void onItemSelected(int position) {
        if(position == 0){
            Log.i(LOG_TAG, String.format("Position %d: Load ingredient list", position));
        } else {
            Step step = recipe.getStepList().get(position - 1);
            Log.i(LOG_TAG, String.format("Clicked on Step %s", step.getShortDescription()));
        }

        if(tabletMode) {
            // create fragment and replace old one
            Fragment fragment;
            Bundle bundle = new Bundle();
            bundle.putParcelable(Recipe.EXTRA_RECIPE_DATA, recipe);
            if (position == 0) {
                fragment = new IngredientsFragment();
            } else {
                bundle.putInt(Recipe.EXTRA_RECIPE_STEP, position);
                fragment = new StepFragment();
            }

            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.selected_step_container, fragment)
                    .commit();

            //title of activity
            setTitle(Helper.getTitle(this, recipe.getName(), position));

        } else {
            // in phone mode start fragment in container activity
            Intent intent = new Intent(DetailActivity.this, StepActivity.class);
            intent.putExtra(Recipe.EXTRA_RECIPE_DATA, recipe);
            intent.putExtra(Recipe.EXTRA_RECIPE_STEP, position);
            startActivity(intent);
        }
    }
}
