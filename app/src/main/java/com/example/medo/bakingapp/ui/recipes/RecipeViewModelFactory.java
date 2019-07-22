package com.example.medo.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.medo.bakingapp.data.RecipeRepository;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeRepository mRepository;

    @Inject
    public RecipeViewModelFactory(RecipeRepository mRepository) {
        this.mRepository = mRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeViewModel(mRepository);
    }
}
