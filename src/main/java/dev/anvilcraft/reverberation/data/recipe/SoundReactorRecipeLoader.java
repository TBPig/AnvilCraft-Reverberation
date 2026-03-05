package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.reverberation.api.Timbre;
import dev.anvilcraft.reverberation.init.AddonBlocks;
import dev.anvilcraft.reverberation.init.AddonItems;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.dubhe.anvilcraft.init.item.ModItemTags;

public class SoundReactorRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES, 2)
            .result(AddonItems.ACOUSTIC_COMPONENT.get())
            .minEnergy(10)
            .save(provider);

        // 示例：需要特定音色的配方
        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES, 4)
            .result(AddonItems.ACOUSTIC_COMPONENT.get(), 2)
            .minEnergy(20)
            .maxEnergy(50)
            .requiresTimbre(new Timbre(AddonBlocks.CLANG_CRYSTAL.get()))
            .requiresTimbre(new Timbre(AddonBlocks.MERGE_SOUND_PILLAR.get()))
            .priority(1)
            .save(provider);
    }
}
