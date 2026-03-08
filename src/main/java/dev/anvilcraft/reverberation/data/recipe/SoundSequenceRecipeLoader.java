package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.reverberation.api.SoundRequire;
import dev.anvilcraft.reverberation.init.AddonMelodies;
import dev.anvilcraft.reverberation.recipe.SoundSequenceEtchingRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;

/**
 * 音序蚀刻配方示例
 */
public class SoundSequenceRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        SoundSequenceEtchingRecipe.builder()
            .requires(Items.IRON_INGOT)
            .intermediate(Items.COPPER_INGOT)
            .result(Items.GOLD_INGOT)
            .soundRequire(SoundRequire.builder()
                .minEnergy(3)
                .build())
            .soundRequire(SoundRequire.builder()
                .energy(4, 6)
                .timbre(Blocks.BUDDING_AMETHYST)
                .melody(AddonMelodies.EQUAL_MELODY)
                .build())
            .loops(2)
            .priority(100)
            .save(provider, "test_sequence");
    }
}
