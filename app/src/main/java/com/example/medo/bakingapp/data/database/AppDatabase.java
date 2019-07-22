package com.example.medo.bakingapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.medo.bakingapp.data.model.Recipe;



@Database(entities = Recipe.class, version = 2, exportSchema = false)
@TypeConverters({IngredientListConverter.class, StepListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "recipe_db";

    public abstract RecipeDao recipeDao();
}
