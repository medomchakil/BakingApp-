package com.example.medo.bakingapp.ui.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.medo.bakingapp.data.RecipeRepository;
import com.example.medo.bakingapp.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;

class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    private final LiveData<List<Recipe>> mAllRecipes;

    @Inject
    RecipeViewModel(RecipeRepository repository) {
        mRecipeRepository = repository;
        mAllRecipes = repository.getAllRecipes();
    }

    LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    void retryFetch() {
        mRecipeRepository.retryFetch();
    }
}
