package dev.anvilcraft.reverberation.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.anvilcraft.reverberation.block.AnvilSoundReactorBlock;
import dev.anvilcraft.reverberation.block.ClangCrystalBlock;
import dev.anvilcraft.reverberation.block.MergeSoundPillarBlock;
import dev.dubhe.anvilcraft.data.AnvilCraftDatagen;
import dev.dubhe.anvilcraft.init.block.ModBlockTags;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import dev.dubhe.anvilcraft.init.item.ModItemTags;
import dev.dubhe.anvilcraft.util.DataGenUtil;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import static dev.anvilcraft.reverberation.AnvilCraftReverberation.REGISTRATE;

@SuppressWarnings("unused")
public class AddonBlocks {
    static {
        REGISTRATE.defaultCreativeTab(AddonItemGroups.ADDON_ITEMS.getKey());
    }

    public static final BlockEntry<ClangCrystalBlock> CLANG_CRYSTAL = REGISTRATE
        .block("clang_crystal", ClangCrystalBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(p -> p.noOcclusion().isValidSpawn(Blocks::never))
        .blockstate(DataGenUtil::noExtraModelOrState)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .simpleItem()
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get(), 4)
            .pattern("AAA")
            .pattern("CBC")
            .pattern("CCC")
            .define('A', AddonItems.ACOUSTIC_COMPONENT)
            .define('B', Items.AMETHYST_BLOCK)
            .define('C', ModItemTags.BRONZE_INGOTS)
            .unlockedBy(AnvilCraftDatagen.hasItem(ModItemTags.BRONZE_INGOTS), AnvilCraftDatagen.has(ModItemTags.BRONZE_INGOTS))
            .save(provider)
        )
        .register();

    public static final BlockEntry<MergeSoundPillarBlock> MERGE_SOUND_PILLAR = REGISTRATE
        .block("merge_sound_pillar", MergeSoundPillarBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .blockstate(DataGenUtil::noExtraModelOrState)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .simpleItem()
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get(), 4)
            .pattern("CCC")
            .pattern("BAB")
            .pattern("CCC")
            .define('A', AddonItems.ACOUSTIC_COMPONENT)
            .define('B', Items.AMETHYST_BLOCK)
            .define('C', ModItemTags.BRONZE_INGOTS)
            .unlockedBy(AnvilCraftDatagen.hasItem(ModItemTags.BRONZE_INGOTS), AnvilCraftDatagen.has(ModItemTags.BRONZE_INGOTS))
            .save(provider)
        )
        .register();

    public static final BlockEntry<AnvilSoundReactorBlock> ANVIL_SOUND_REACTOR = REGISTRATE
        .block("anvil_sound_reactor", AnvilSoundReactorBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .blockstate(DataGenUtil::noExtraModelOrState)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .simpleItem()
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
            .pattern("C C")
            .pattern("A A")
            .pattern("CCC")
            .define('A', AddonItems.ACOUSTIC_COMPONENT)
            .define('C', ModItemTags.BRONZE_INGOTS)
            .unlockedBy(AnvilCraftDatagen.hasItem(ModItemTags.BRONZE_INGOTS), AnvilCraftDatagen.has(ModItemTags.BRONZE_INGOTS))
            .save(provider)
        )
        .register();

    public static final BlockEntry<Block> ECHO_METAL_BLOCK = REGISTRATE
        .block("echo_metal_block", Block::new)
        .lang("Block of Echo Metal")
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(properties -> properties.lightLevel(state -> 9).noOcclusion().emissiveRendering(ModBlocks::always))
        .tag(
            BlockTags.BEACON_BASE_BLOCKS,
            BlockTags.MINEABLE_WITH_PICKAXE,
            BlockTags.NEEDS_IRON_TOOL,
            ModBlockTags.OVERSEER_BASE,
            Tags.Blocks.STORAGE_BLOCKS
        )
        .item()
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get())
            .pattern("AAA")
            .pattern("AAA")
            .pattern("AAA")
            .define('A', AddonItems.ECHO_METAL_INGOT)
            .unlockedBy(
                AnvilCraftDatagen.hasItem(AddonItems.ECHO_METAL_INGOT),
                RegistrateRecipeProvider.has(AddonItems.ECHO_METAL_INGOT)
            )
            .save(provider))
        .register();

    public static void register() {
    }
}
