package com.example.medo.bakingapp.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.medo.bakingapp.BakingApplication;

import javax.inject.Inject;


public class RecipeIntentService extends IntentService {

    private static final String LOG_TAG = RecipeIntentService.class.getSimpleName();

    @Inject
    NetworkDataSource networkDataSource;
    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BakingApplication.getComponent(this).inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Intent service started");
        networkDataSource.fetchRecipes();
    }
}
