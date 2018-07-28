package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

    @BindView(R.id.tv_recipe_name) TextView mRecipeName;
    @BindView(R.id.tv_recipe_ingredients) TextView mIngredients;
    @BindView(R.id.tv_recipe_servings) TextView mServings;
    @BindView(R.id.tv_recipe_steps) TextView mSteps;
    @BindView(R.id.iv_recipe_image) ImageView mRecipeImage;

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

        Log.d("TESTTTTT", "Recipe Image is");

        Log.d("TESTTTTT", recipe.getImage());


        mRecipeName.setText( recipe.getName());
        mIngredients.setText( context.getString(R.string.recipe_ingredients, recipe.getIngredients().size()) );
        mServings.setText( context.getString(R.string.recipe_servings, recipe.getServings() ));
        mSteps.setText( context.getString(R.string.recipe_steps, recipe.getIngredients().size()) );




        if(!recipe.getImage().equals("")){
            Picasso.with(context).load(recipe.getImage())
                    .placeholder(R.drawable.ic_local_pizza_black_24dp)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(mRecipeImage);
        }


    }

    @Override
    public void onClick(View v) {
        clickListener.onRecyclerItemClicked( getAdapterPosition() );
    }
}
