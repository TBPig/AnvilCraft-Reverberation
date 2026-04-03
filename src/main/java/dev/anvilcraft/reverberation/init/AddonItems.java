package dev.anvilcraft.reverberation.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.data.AnvilCraftDatagen;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

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

    public static final ItemEntry<Item> INCOMPLETE_ECHO_METAL_INGOT = REGISTRATE
        .item("incomplete_echo_metal_ingot", Item::new)
        .removeTab(AddonItemGroups.ADDON_ITEMS.getKey())
        .register();

    public static final ItemEntry<Item> ECHO_METAL_INGOT = REGISTRATE
        .item("echo_metal_ingot", Item::new)
        .tag(Tags.Items.INGOTS, ItemTags.BEACON_PAYMENT_ITEMS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(AddonBlocks.ECHO_METAL_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(AddonBlocks.ECHO_METAL_BLOCK.asItem()),
                    AnvilCraftDatagen.has(AddonBlocks.ECHO_METAL_BLOCK)
                )
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', AddonItems.ECHO_METAL_NUGGET)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(AddonItems.ECHO_METAL_NUGGET),
                    RegistrateRecipeProvider.has(AddonItems.ECHO_METAL_NUGGET)
                )
                .save(provider);
        })
        .register();

    public static final ItemEntry<Item> ECHO_METAL_NUGGET = REGISTRATE
        .item("echo_metal_nugget", Item::new)
        .tag(Tags.Items.NUGGETS)
        .recipe((ctx, provider) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
            .requires(AddonItems.ECHO_METAL_INGOT)
            .unlockedBy(AnvilCraftDatagen.hasItem(AddonItems.ECHO_METAL_INGOT), AnvilCraftDatagen.has(AddonItems.ECHO_METAL_INGOT))
            .save(provider))
        .register();

    public static void register() {
    }
}
