package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.reverberation.api.SoundRequire;
import dev.anvilcraft.reverberation.recipe.SoundSequenceReactorRecipe;
import net.minecraft.world.item.Items;

/**
 * 音序蚀刻配方示例
 */
public class SoundSequenceRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        SoundSequenceReactorRecipe.builder()
            .requires(Items.IRON_INGOT)
            .intermediate(Items.COPPER_INGOT)
            .result(Items.GOLD_INGOT)
            .soundRequire(SoundRequire.builder()
                .energy(3, 10)
                .build())
            .soundRequire(SoundRequire.builder()
                .energy(4, 6)
                .build())
            .priority(100)
            .save(provider, "test_sequence");
    }
}
