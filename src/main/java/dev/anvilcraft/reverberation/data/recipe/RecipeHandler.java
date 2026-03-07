package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;

public class RecipeHandler {
    public static void init(RegistrateRecipeProvider provider) {
        SoundReactorRecipeLoader.init(provider);
        SoundSequenceRecipeLoader.init(provider);
    }
}
