package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepViewHolder> {


    private List<Step> steps;
    private Context context;
    final private RecyclerClickListener clickListener;

    public RecipeStepsAdapter(Context context, List<Step> steps, RecyclerClickListener clickListener) {
        this.steps = steps;
        this.context = context;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
        RecipeStepViewHolder viewHolder = new RecipeStepViewHolder(view, context, clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        holder.bind(steps.get(position) , context);
    }


    @Override
    public int getItemCount() {
        if(steps != null){
            return steps.size();
        }else{
            return 0;
        }
    }
}
