package com.example.medo.bakingapp.di;

import com.example.medo.bakingapp.data.network.RecipeIntentService;
import com.example.medo.bakingapp.di.modules.ContextModule;
import com.example.medo.bakingapp.di.modules.ExecutorsModule;
import com.example.medo.bakingapp.di.modules.NetworkModule;
import com.example.medo.bakingapp.di.modules.RoomModule;
import com.example.medo.bakingapp.ui.recipes.RecipeFragment;
import com.example.medo.bakingapp.widget.ListWidgetService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RoomModule.class, NetworkModule.class, ExecutorsModule.class, ContextModule.class})
public interface BakingComponent {

    void inject(RecipeFragment fragment);

    void inject(RecipeIntentService service);

    void inject(ListWidgetService.ListRemoteViewsFactory service);

}
