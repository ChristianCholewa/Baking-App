package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IngredientsFragment extends Fragment {

    @BindView(R.id.rv_ingredients_list) RecyclerView recyclerView;
    private Unbinder unbinder;

    public IngredientsFragment () {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //similar to onCreate for an activity
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Context context = getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            Recipe recipe = bundle.getParcelable(Recipe.EXTRA_RECIPE_DATA);
            if(recipe != null) {
                IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(context, recipe.getIngredientList());
                recyclerView.setAdapter(ingredientsAdapter);
            }
        }

        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
