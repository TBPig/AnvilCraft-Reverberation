package dev.anvilcraft.reverberation.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.anvilcraft.reverberation.init.AddonItems;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import net.minecraft.world.level.block.Blocks;

public class SoundReactorRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        SoundReactorRecipe.builder()
            .requires(ModItemTags.BRONZE_PLATES, 2)
            .result(AddonItems.ACOUSTIC_COMPONENT.get())
            .requiresTimbre(Blocks.IRON_BLOCK)
            .requiresTimbre(ModBlocks.BRONZE_BLOCK.get())
            .minEnergy(10)
            .save(provider);
    }
}
