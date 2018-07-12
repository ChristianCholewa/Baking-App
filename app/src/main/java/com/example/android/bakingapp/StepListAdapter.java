package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder>{

    private Recipe recipe;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;
    private Context context;

    StepListAdapter(Context context, Recipe recipe) {
        this.inflater = LayoutInflater.from(context);
        this.recipe = recipe;
        this.context = context;
    }

    @NonNull
    @Override
    public StepListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.step_item, parent, false);
        return new StepListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepListAdapter.ViewHolder holder, int position) {
        if(position == 0){
            holder.textViewTitel.setText(context.getString(R.string.detail_ingredient_titel));
        } else {
            holder.textViewTitel.setText(recipe.getStepList().get(position - 1).getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        if(recipe == null || recipe.getStepList() == null){
            return 1; // ingredient list
        } else {
            return recipe.getStepList().size() + 1; // + ingredient list
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.rv_step_item_titel) TextView textViewTitel;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    void setClickListener(StepListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
