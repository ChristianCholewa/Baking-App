package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>{

    private List<Recipe> recipeList;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;
    private Context context;

    RecipesAdapter(Context context, List<Recipe> data) {
        this.inflater = LayoutInflater.from(context);
        this.recipeList = data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = recipeList.get(position).getImage();
        if(!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(context).load(imageUrl).into(holder.imageView);
        }

        holder.textView.setText(recipeList.get(position).getName());
        holder.textViewServings.setText(context.getString(R.string.recipe_servings) + Integer.toString(recipeList.get(position).getServings()));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        if(recipeList == null){
            return 0;
        }else {
            return recipeList.size();
        }
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.rv_item_image) ImageView imageView;
        @BindView(R.id.rv_item_titel) TextView textView;
        @BindView(R.id.rv_item_servings) TextView textViewServings;
        @BindView(R.id.rv_cardview) CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Recipe getItem(int id) {
        return recipeList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
