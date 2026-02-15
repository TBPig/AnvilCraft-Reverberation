package dev.anvilcraft.reverberation.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.dubhe.anvilcraft.data.AnvilCraftDatagen;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static dev.anvilcraft.reverberation.AnvilCraftReverberation.REGISTRATE;

@SuppressWarnings("unused")
public class AddonItems {
    static {
        REGISTRATE.defaultCreativeTab(AddonItemGroups.ADDON_ITEMS.getKey());
    }

    public static final ItemEntry<Item> ACOUSTIC_COMPONENT = REGISTRATE
        .item("acoustic_component", Item::new)
        .recipe((ctx, provider) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get())
            .requires(Items.AMETHYST_BLOCK)
            .requires(ModItemTags.BRONZE_INGOTS)
            .requires(ModItemTags.BRONZE_INGOTS)
            .requires(ModItemTags.BRONZE_INGOTS)
            .unlockedBy(AnvilCraftDatagen.hasItem(ModItemTags.BRONZE_INGOTS), AnvilCraftDatagen.has(ModItemTags.BRONZE_INGOTS))
            .save(provider))
        .register();

    public static void register() {
    }
}
