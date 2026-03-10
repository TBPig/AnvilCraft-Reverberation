package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.reverberation.recipe.component.SoundPredicate;
import dev.anvilcraft.reverberation.init.AddonItems;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class SoundReactorRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES)
            .result(AddonItems.ACOUSTIC_COMPONENT.get())
            .sound(SoundPredicate.builder()
                .minEnergy(12)
                .timbre(Blocks.AMETHYST_BLOCK)
                .build())
            .save(provider, "acoustic_component_0");
        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES)
            .result(AddonItems.ACOUSTIC_COMPONENT.get())
            .sound(SoundPredicate.builder()
                .timbre(Blocks.BUDDING_AMETHYST)
                .build())
            .save(provider, "acoustic_component_1");

        SoundReactorRecipe.builder()
            .requires(AddonItems.INCOMPLETE_ECHO_METAL_INGOT)
            .result(Items.ECHO_SHARD)
            .sound(SoundPredicate.EMPTY)
            .priority(-1)
            .save(provider);
    }
}
