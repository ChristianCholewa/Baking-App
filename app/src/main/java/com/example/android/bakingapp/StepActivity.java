package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utils.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepActivity extends AppCompatActivity {

    private static final String LOG_TAG = StepActivity.class.getSimpleName();

    @BindView(R.id.button_forward) Button buttonForward;
    @BindView(R.id.button_back) Button buttonBack;

    private static final String BUNDLE_RECIPE = "BUNDLE_RECIPE";
    private static final String BUNDLE_STEP = "BUNDLE_STEP";
    private Recipe recipe;
    private int selectedStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        // don't do all this if devices is only rotated
        if(savedInstanceState == null) {
            Log.i(LOG_TAG, "Creating new fragment");

            // make sure extras are set
            Intent intent = getIntent();
            if (intent == null) {
                Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
                finish();
                return;
            } else if (!intent.hasExtra(Recipe.EXTRA_RECIPE_DATA)) {
                Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
                finish();
                return;
            } else if (!intent.hasExtra(Recipe.EXTRA_RECIPE_STEP)) {
                Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
                finish();
                return;
            } else {
                recipe = intent.getParcelableExtra(Recipe.EXTRA_RECIPE_DATA);
                selectedStep = intent.getIntExtra(Recipe.EXTRA_RECIPE_STEP, 0);
            }

            activateFragment(true);
        } else {
            selectedStep = savedInstanceState.getInt(BUNDLE_STEP);
            recipe =savedInstanceState.getParcelable(BUNDLE_RECIPE);
        }

        setTextOfButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_STEP, selectedStep);
        outState.putParcelable(BUNDLE_RECIPE, recipe);
    }

    private void setTextOfButtons(){
        if(selectedStep == 0){
            buttonForward.setText(getString(R.string.step_button_Next));
            buttonBack.setText(getString(R.string.step_button_Close));
        } else if(selectedStep == recipe.getStepList().size() - 1){
            buttonForward.setText(getString(R.string.step_button_Close));
            buttonBack.setText(getString(R.string.step_button_previous));
        } else {
            buttonForward.setText(getString(R.string.step_button_Next));
            buttonBack.setText(getString(R.string.step_button_previous));
        }        
    }
    
    private void activateFragment(boolean addFragment){

        //title of activity
        setTitle(Helper.getTitle(this, recipe.getName(), selectedStep));

        //choose fragment
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putParcelable(Recipe.EXTRA_RECIPE_DATA, recipe);

        if (selectedStep == 0) {
            fragment = new IngredientsFragment();
        } else {
            bundle.putInt(Recipe.EXTRA_RECIPE_STEP, selectedStep);
            fragment = new StepFragment();
        }

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(addFragment) {
            fragmentManager.beginTransaction()
                    .add(R.id.selected_step_container, fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.selected_step_container, fragment)
                    .commit();
        }
    }

    @OnClick(R.id.button_back)
    public void onButtonBack(View view) {
        Log.d(LOG_TAG, "onButtonBack");

        if(selectedStep == 0){
            finish();
            return;
        }

        selectedStep--;
        activateFragment(false);
        setTextOfButtons();
    }

    @OnClick(R.id.button_forward)
    public void onButtonForward(View view) {
        Log.d(LOG_TAG, "onButtonForward");

        if(selectedStep == recipe.getStepList().size() - 1){
            finish();
            return;
        }

        selectedStep++;
        activateFragment(false);
        setTextOfButtons();
    }

    // make home button act as back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
