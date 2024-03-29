package com.example.medo.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.medo.bakingapp.data.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe WHERE id = :id")
    Recipe getSelectedRecipe(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void bulkInsert(List<Recipe> recipes);

    @Query("DELETE FROM recipe")
    void deleteAllRecipes();
}
