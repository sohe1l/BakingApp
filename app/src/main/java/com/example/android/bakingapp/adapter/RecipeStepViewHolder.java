package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepViewHolder extends RecyclerView.ViewHolder
implements View.OnClickListener{

    @BindView(R.id.tv_recipe_step_desc) TextView tv_desc;
    @BindView(R.id.tv_recipe_step_short_desc) TextView tv_short_desc;

    Context context;
    RecyclerClickListener clickListener;

    public RecipeStepViewHolder(View itemView, Context context,RecyclerClickListener clickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    public void bind(Step step, Context context){
        tv_desc.setText( step.getDescription());
        tv_short_desc.setText( step.getShortDescription());
    }

    @Override
    public void onClick(View v) {
        clickListener.onRecyclerItemClicked( getAdapterPosition() );
    }
}
