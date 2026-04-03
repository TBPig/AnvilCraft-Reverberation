package dev.anvilcraft.reverberation.integration.jei.recipe;

import dev.anvilcraft.reverberation.recipe.component.SoundPredicate;
import dev.anvilcraft.reverberation.init.AddonBlocks;
import dev.anvilcraft.reverberation.init.AddonRecipeType;
import dev.anvilcraft.reverberation.integration.jei.AddonJeiPlugin;
import dev.anvilcraft.reverberation.recipe.SoundSequenceEtchingRecipe;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

public class SoundSequenceEtchingCategory implements IRecipeCategory<RecipeHolder<SoundSequenceEtchingRecipe>> {
    public static final int WIDTH = 202;
    public static final int HEIGHT = 140;

    private final IDrawable icon;
    private final Component title;

    protected final IDrawable arrowDefault;

    public SoundSequenceEtchingCategory(IGuiHelper helper) {
        icon = helper.createDrawableItemStack(AddonBlocks.ANVIL_SOUND_REACTOR.asStack());
        title = Component.translatable("gui.anvilcraft_reverberation.category.sound_sequence_etching");
        this.arrowDefault = JeiRenderHelper.getArrowDefault(helper);
    }

    @Override
    public RecipeType<RecipeHolder<SoundSequenceEtchingRecipe>> getRecipeType() {
        return AddonJeiPlugin.SOUND_SEQUENCE_ETCHING;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
        IRecipeLayoutBuilder builder,
        RecipeHolder<SoundSequenceEtchingRecipe> recipeHolder,
        IFocusGroup focuses
    ) {
        SoundSequenceEtchingRecipe recipe = recipeHolder.value();

        // Get ingredient ingredient
        builder.addSlot(RecipeIngredientRole.INPUT, 41, 4)
            .addIngredients(Ingredient.of(recipe.ingredient().getItems()));

        // Add final result
        builder.addSlot(RecipeIngredientRole.OUTPUT, 150, 4).addItemStack(recipe.result());

        // Add intermediate (shown as a smaller output)
        builder.addSlot(RecipeIngredientRole.OUTPUT, 93, 4).addItemStack(recipe.intermediate());

        // Add catalyst slot (the sound reactor block)
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST)
            .addItemStack(AddonBlocks.ANVIL_SOUND_REACTOR.asStack());
    }

    @Override
    public void draw(
        RecipeHolder<SoundSequenceEtchingRecipe> recipeHolder,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY
    ) {
        // Draw arrows
        arrowDefault.draw(guiGraphics, 70, 8);
        arrowDefault.draw(guiGraphics, 120, 8);

        // Draw recipe info
        SoundSequenceEtchingRecipe recipe = recipeHolder.value();
        guiGraphics.drawString(
            Minecraft.getInstance().font,
            Component.translatable("tooltip.anvilcraft_reverberation.sound_sequence.loops", recipe.loops()),
            40,
            20,
            0xFF000000,
            false
        );
        guiGraphics.drawString(
            Minecraft.getInstance().font,
            Component.translatable("tooltip.anvilcraft_reverberation.sound_sequence.all_steps", recipe.allStepNum()),
            110,
            20,
            0xFF000000,
            false
        );

        // Draw first step requirement
        for (int i = 0; i < recipe.steps().size(); i++) {
            guiGraphics.drawString(
                Minecraft.getInstance().font,
                SoundPredicate.getFullDescription(recipe.steps().get(i)).getString(),
                1,
                30 + i * 10,
                0xFF000000,
                false
            );
        }
    }

    public static void registerRecipes(mezz.jei.api.registration.IRecipeRegistration registration) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.getSingleplayerServer() == null) return;
        registration.addRecipes(
            AddonJeiPlugin.SOUND_SEQUENCE_ETCHING,
            JeiRecipeUtil.getRecipeHoldersFromType(AddonRecipeType.SOUND_SEQUENCE_ETCHING_TYPE.get())
        );
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(AddonBlocks.ANVIL_SOUND_REACTOR), AddonJeiPlugin.SOUND_SEQUENCE_ETCHING);
        registration.addRecipeCatalyst(new ItemStack(AddonBlocks.MERGE_SOUND_PILLAR), AddonJeiPlugin.SOUND_SEQUENCE_ETCHING);
    }
}
