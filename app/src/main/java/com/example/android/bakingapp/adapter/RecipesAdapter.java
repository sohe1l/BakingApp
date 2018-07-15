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

public class RecipesAdapter extends RecyclerView.Adapter<RecipeViewHolder> {



    private List<Recipe> recipes;
    private Context context;

    final private RecyclerClickListener clickListener;

    public RecipesAdapter(Context context, List<Recipe> recipes, RecyclerClickListener clickListener){
        this.clickListener = clickListener;
        this.context = context;
        this.recipes = recipes;
    }



    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_item, parent, false);

        RecipeViewHolder viewHolder = new RecipeViewHolder(view, context, clickListener);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        holder.bind(recipes.get(position) , context);
    }


    @Override
    public int getItemCount() {
        if(recipes != null){
            return recipes.size();
        }else{
            return 0;
        }
    }

}
