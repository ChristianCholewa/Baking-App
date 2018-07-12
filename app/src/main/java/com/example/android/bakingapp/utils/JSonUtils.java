package com.example.android.bakingapp.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSonUtils {

    private static final String LOG_TAG = JSonUtils.class.getSimpleName();

    //recipe tags
    private final static String JSON_ID = "id";
    private final static String JSON_NAME = "name";
    private final static String JSON_SERVINGS = "servings";
    private final static String JSON_IMAGE = "image";
    private final static String JSON_INGREDIENTS = "ingredients";
    private final static String JSON_STEPS = "steps";

    //ingredients tags
    private final static String JSON_INGREDIENT_QUANTITY = "quantity";
    private final static String JSON_INGREDIENT_MEASURE = "measure";
    private final static String JSON_INGREDIENT_INGREDIENT = "ingredient";

    //steps tags
    private final static String JSON_STEP_ID = "id";
    private final static String JSON_STEP_SHORT_DESCRIPTION = "shortDescription";
    private final static String JSON_STEP_DESCRIPTION = "description";
    private final static String JSON_STEP_VIDEO_URL = "videoURL";
    private final static String JSON_STEP_THUMBNAIL_URL = "thumbnailURL";

    public static List<Recipe> ParseJson(String jsonString){
        List<Recipe> recipeList = new ArrayList<>();

        if(!TextUtils.isEmpty(jsonString)){
            try {
                JSONArray recipeArray = new JSONArray(jsonString);
                Log.i(LOG_TAG, String.format("Count recipes: %d", recipeArray.length()));

                for (int i = 0; i < recipeArray.length(); i++){
                    JSONObject jsonObjectRecipe = recipeArray.getJSONObject(i);

                    Log.d(LOG_TAG, String.format("recipe: %s", jsonObjectRecipe.getString(JSON_NAME)));

                    JSONArray jsonArrayIngredients = jsonObjectRecipe.getJSONArray(JSON_INGREDIENTS);
                    JSONArray jsonArraySteps = jsonObjectRecipe.getJSONArray(JSON_STEPS);

                    Recipe recipe = new Recipe();
                    recipe.setId(jsonObjectRecipe.getInt(JSON_ID));
                    recipe.setName(jsonObjectRecipe.getString(JSON_NAME));
                    recipe.setServings(jsonObjectRecipe.getInt(JSON_SERVINGS));
                    recipe.setImage(jsonObjectRecipe.getString(JSON_IMAGE));
                    recipe.setIngredientList(getIngredientListFromJSON(jsonArrayIngredients));
                    recipe.setStepList(getStepListFromJSON(jsonArraySteps));

                    recipeList.add(recipe);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return recipeList;
    }

    private static List<Ingredient> getIngredientListFromJSON(JSONArray jsonArrayIngredients){
        List<Ingredient> ingredientList = new ArrayList<>();

        Log.d(LOG_TAG, String.format("Count ingredients: %d", jsonArrayIngredients.length()));

        try {
            for (int i = 0; i < jsonArrayIngredients.length(); i++) {
                JSONObject jsonObjectIngredient = jsonArrayIngredients.getJSONObject(i);

                Log.d(LOG_TAG, String.format("Ingredient: %s", jsonObjectIngredient.get(JSON_INGREDIENT_INGREDIENT)));

                Ingredient ingredient = new Ingredient();
                ingredient.setQuantity(jsonObjectIngredient.getDouble(JSON_INGREDIENT_QUANTITY));
                ingredient.setIngredient(jsonObjectIngredient.getString(JSON_INGREDIENT_INGREDIENT));
                ingredient.setMeasure(jsonObjectIngredient.getString(JSON_INGREDIENT_MEASURE));

                ingredientList.add(ingredient);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredientList;
    }

    private static List<Step> getStepListFromJSON(JSONArray jsonArraySteps){
        List<Step> stepList = new ArrayList<>();

        Log.d(LOG_TAG, String.format("Count steps: %d", jsonArraySteps.length()));

        JSONObject jsonObjectStep;

        try {
            for (int i = 0; i < jsonArraySteps.length(); i++) {
                jsonObjectStep = jsonArraySteps.getJSONObject(i);

                Log.d(LOG_TAG, String.format("Step: %s", jsonObjectStep.get(JSON_STEP_SHORT_DESCRIPTION)));

                Step step = new Step();
                step.setId(jsonObjectStep.getInt(JSON_STEP_ID));
                step.setShortDescription(jsonObjectStep.getString(JSON_STEP_SHORT_DESCRIPTION));
                step.setDescription(jsonObjectStep.getString(JSON_STEP_DESCRIPTION));
                step.setVideoUrl(jsonObjectStep.getString(JSON_STEP_VIDEO_URL));
                step.setThumbnailUrl(jsonObjectStep.getString(JSON_STEP_THUMBNAIL_URL));

                stepList.add(step);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stepList;
    }
}
