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

public class StepListFragment extends Fragment implements StepListAdapter.ItemClickListener {

    @BindView(R.id.rv_steplist) RecyclerView recyclerView;
    private Unbinder unbinder;

    private static final String LOG_TAG = StepListFragment.class.getSimpleName();

    Context context;

    // interface that triggers a callback in the host activity
    OnStepItemClickListener mCallback;
    public interface OnStepItemClickListener {
        void onItemSelected(int position);
    }

    // make sure the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        try {
            mCallback = (OnStepItemClickListener) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnStepItemClickListener");
        }
    }

    public StepListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //similar to onCreate for an activity
        View rootView = inflater.inflate(R.layout.fragment_steplist, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            Recipe recipe = bundle.getParcelable(Recipe.EXTRA_RECIPE_DATA);
            StepListAdapter stepListAdapter = new StepListAdapter(context, recipe);
            stepListAdapter.setClickListener(StepListFragment.this);
            recyclerView.setAdapter(stepListAdapter);
        }

        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        //trigger the callback function in the host activity
        mCallback.onItemSelected(position);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
