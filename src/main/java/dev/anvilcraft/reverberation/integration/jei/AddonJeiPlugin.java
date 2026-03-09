package dev.anvilcraft.reverberation.integration.jei;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.init.AddonBlocks;
import dev.anvilcraft.reverberation.integration.jei.recipe.SoundReactorCategory;
import dev.anvilcraft.reverberation.integration.jei.recipe.SoundSequenceEtchingCategory;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.anvilcraft.reverberation.recipe.SoundSequenceEtchingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class AddonJeiPlugin implements IModPlugin {

    public static final RecipeType<RecipeHolder<SoundSequenceEtchingRecipe>> SOUND_SEQUENCE_ETCHING =
        createRecipeHolderType("sound_sequence_etching");

    public static final RecipeType<RecipeHolder<SoundReactorRecipe>> SOUND_REACTOR =
        createRecipeHolderType("sound_reactor");


    @Override
    public ResourceLocation getPluginUid() {
        return AnvilCraftReverberation.of("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new SoundReactorCategory(helper));
        registration.addRecipeCategories(new SoundSequenceEtchingCategory(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        SoundReactorCategory.registerRecipes(registration);
        SoundSequenceEtchingCategory.registerRecipes(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        SoundReactorCategory.registerRecipeCatalysts(registration);
        SoundSequenceEtchingCategory.registerRecipeCatalysts(registration);
    }

    public static <R extends Recipe<?>> RecipeType<RecipeHolder<R>> createRecipeHolderType(String name) {
        return RecipeType.createRecipeHolderType(AnvilCraftReverberation.of(name));
    }
}
