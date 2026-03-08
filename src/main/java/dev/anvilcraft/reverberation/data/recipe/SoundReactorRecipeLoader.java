package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.reverberation.api.SoundRequire;
import dev.anvilcraft.reverberation.init.AddonItems;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import net.minecraft.world.level.block.Blocks;

public class SoundReactorRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES)
            .result(AddonItems.ACOUSTIC_COMPONENT.get())
            .soundRequire(SoundRequire.builder()
                .minEnergy(12)
                .timbre(Blocks.AMETHYST_BLOCK)
                .build())
            .save(provider, "acoustic_component_0");
        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES)
            .result(AddonItems.ACOUSTIC_COMPONENT.get())
            .soundRequire(SoundRequire.builder()
                .timbre(Blocks.BUDDING_AMETHYST)
                .build())
            .save(provider, "acoustic_component_1");
    }
}
