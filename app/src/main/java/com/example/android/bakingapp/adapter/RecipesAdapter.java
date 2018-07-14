package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {



    private List<Recipe> recipes;
    private Context context;

    public RecipesAdapter(Context context, List<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }



    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();


        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_item, parent, false);

        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        holder.bind(position, context);
    }


    @Override
    public int getItemCount() {
        if(recipes != null){
            return recipes.size();
        }else{
            return 0;
        }
    }





    class RecipeViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_recipe_name) TextView mRecipeName;
        @BindView(R.id.tv_recipe_ingredients) TextView mIngredients;
        @BindView(R.id.tv_recipe_servings) TextView mServings;
        @BindView(R.id.tv_recipe_steps) TextView mSteps;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(int p, Context context){

            Recipe r = recipes.get(p);
            mRecipeName.setText( r.getName());
            mIngredients.setText( context.getString(R.string.recipe_ingredients, r.getIngredients().size()) );
            mServings.setText( context.getString(R.string.recipe_servings, r.getServings() ));
            mSteps.setText( context.getString(R.string.recipe_steps, r.getIngredients().size()) );

        }
    }

}
