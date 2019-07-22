package com.example.medo.bakingapp.data.network;

import com.example.medo.bakingapp.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("baking.json")
    Call<List<Recipe>> getRecipe();
}
