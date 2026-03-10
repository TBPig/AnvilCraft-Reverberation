package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.reverberation.recipe.component.SoundPredicate;
import dev.anvilcraft.reverberation.init.AddonItems;
import dev.anvilcraft.reverberation.init.AddonMelodies;
import dev.anvilcraft.reverberation.recipe.SoundSequenceEtchingRecipe;
import net.minecraft.world.item.Items;

/**
 * 音序蚀刻配方示例
 */
public class SoundSequenceRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        SoundSequenceEtchingRecipe.builder()
            .requires(Items.ECHO_SHARD)
            .intermediate(AddonItems.INCOMPLETE_ECHO_METAL_INGOT.get())
            .result(AddonItems.ECHO_METAL_INGOT.get())
            .soundRequire(SoundPredicate.builder()
                .melody(AddonMelodies.LOWER_MELODY)
                .build())
            .loops(4)
            .priority(100)
            .save(provider);
    }
}
