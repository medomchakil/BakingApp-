package com.example.medo.bakingapp.ui.recipes;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.example.medo.bakingapp.R;
import com.example.medo.bakingapp.utils.BaseActivity;

import java.util.Objects;

public class MainActivity extends BaseActivity {

    private static final String RECIPE_FRAG = "com.example.medo.bakingapp.ui.recipes.DETAIL.FRAG";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }

        FragmentManager manager = getSupportFragmentManager();
        RecipeFragment recipeFragment = (RecipeFragment) manager.findFragmentByTag(RECIPE_FRAG);

        if (recipeFragment == null) {
            recipeFragment = new RecipeFragment();
        }

        addFragmentToActivity(
                manager,
                recipeFragment,
                R.id.recipe_fragment,
                RECIPE_FRAG
        );

    }

}
