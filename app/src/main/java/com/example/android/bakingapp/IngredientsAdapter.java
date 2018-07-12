package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.data.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<Ingredient> ingredientList;
    private LayoutInflater mInflater;

    IngredientsAdapter(Context context, List<Ingredient> ingredientList) {
        this.mInflater = LayoutInflater.from(context);
        this.ingredientList = ingredientList;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTitle.setText(ingredientList.get(position).getIngredient());
        holder.textViewQuantity.setText(Double.toString(ingredientList.get(position).getQuantity()));
        holder.textViewMeasure.setText(ingredientList.get(position).getMeasure());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        if(ingredientList == null){
            return 0;
        }else {
            return ingredientList.size();
        }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredients_item_title) TextView textViewTitle;
        @BindView(R.id.ingredients_item_quantity) TextView textViewQuantity;
        @BindView(R.id.ingredients_item_measure) TextView textViewMeasure;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}