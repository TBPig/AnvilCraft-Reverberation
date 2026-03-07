package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.reverberation.api.SoundRequire;
import dev.anvilcraft.reverberation.init.AddonItems;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;

public class SoundReactorRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES, 2)
            .result(AddonItems.ACOUSTIC_COMPONENT.get())
            .soundRequire(SoundRequire.builder()
                .minEnergy(8)
                .build())
            .save(provider, "acoustic_component_0");

        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES)
            .result(AddonItems.ACOUSTIC_COMPONENT.get())
            .soundRequire(SoundRequire.builder()
                .minEnergy(16)
                .timbre(ModBlocks.BRONZE_BLOCK.get())
                .build())
            .priority(1)
            .save(provider, "acoustic_component_1");
    }
}
