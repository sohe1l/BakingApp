package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

    @BindView(R.id.tv_recipe_name) TextView mRecipeName;
    @BindView(R.id.tv_recipe_ingredients) TextView mIngredients;
    @BindView(R.id.tv_recipe_servings) TextView mServings;
    @BindView(R.id.tv_recipe_steps) TextView mSteps;

    Context context;
    RecyclerClickListener clickListener;

    public RecipeViewHolder(View itemView, Context context, RecyclerClickListener clickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    public void bind(Recipe recipe, Context context){

        mRecipeName.setText( recipe.getName());
        mIngredients.setText( context.getString(R.string.recipe_ingredients, recipe.getIngredients().size()) );
        mServings.setText( context.getString(R.string.recipe_servings, recipe.getServings() ));
        mSteps.setText( context.getString(R.string.recipe_steps, recipe.getIngredients().size()) );

    }

    @Override
    public void onClick(View v) {
        clickListener.onRecyclerItemClicked( getAdapterPosition() );
    }
}
