package com.example.medo.bakingapp.data;

import android.arch.lifecycle.LiveData;

import com.example.medo.bakingapp.AppExecutors;
import com.example.medo.bakingapp.data.database.RecipeDao;
import com.example.medo.bakingapp.data.model.Recipe;
import com.example.medo.bakingapp.data.network.NetworkDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class RecipeRepository {
    private final RecipeDao mRecipeDao;
    private final NetworkDataSource mNetworkDataSource;
    private AppExecutors mExecutors;
    private boolean mInitialized = false;

    @Inject
    public RecipeRepository(RecipeDao recipeDao, NetworkDataSource networkDataSource, AppExecutors executors) {
        mRecipeDao = recipeDao;
        mNetworkDataSource = networkDataSource;
        mExecutors = executors;

        LiveData<List<Recipe>> networkData = mNetworkDataSource.getRecipes();
        networkData.observeForever(newRecipes -> mExecutors.diskIO().execute(() -> {
            deleteOldData();
            mRecipeDao.bulkInsert(newRecipes);
        }));
    }

    private synchronized void initializeData() {

        if (mInitialized) return;
        mInitialized = true;
        startFetchRecipeService();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        initializeData();
        return mRecipeDao.getAllRecipes();
    }

    public void retryFetch() {
        mNetworkDataSource.fetchRecipes();
    }

    private void deleteOldData() {
        mRecipeDao.deleteAllRecipes();
    }

    public Recipe getRecipe(int id) {
        return mRecipeDao.getSelectedRecipe(id);
    }

    private void startFetchRecipeService() {
        mExecutors.diskIO().execute(mNetworkDataSource::startFetchRecipeService);
    }
}
