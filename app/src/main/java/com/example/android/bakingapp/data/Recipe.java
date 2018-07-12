package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    public static final String EXTRA_RECIPE_DATA = "EXTRA_RECIPE_DATA";
    public static final String EXTRA_RECIPE_STEP = "EXTRA_RECIPE_STEP";

    public Recipe(){}

    //fields
    private int id;
    private String name;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private int servings;
    private String image;

    // getter / setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientsList) {
        this.ingredientList = ingredientsList;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepsList) {
        this.stepList = stepsList;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeList(ingredientList);
        dest.writeList(stepList);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    private void readFromParcel(Parcel in) {
        ingredientList = new ArrayList<>();
        stepList = new ArrayList<>();

        id = in.readInt();
        name = in.readString();
        in.readList(ingredientList, Ingredient.class.getClassLoader());
        in.readList(stepList, Step.class.getClassLoader());
        servings = in.readInt();
        image = in.readString();
    }

    private Recipe(Parcel in){
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
