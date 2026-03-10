package dev.anvilcraft.reverberation.integration.jei.recipe;

import dev.anvilcraft.reverberation.recipe.component.SoundPredicate;
import dev.anvilcraft.reverberation.init.AddonBlocks;
import dev.anvilcraft.reverberation.init.AddonRecipeType;
import dev.anvilcraft.reverberation.integration.jei.AddonJeiPlugin;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.dubhe.anvilcraft.client.support.RenderSupport;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import mezz.jei.api.gui.ITickTimer;
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
import net.minecraft.world.level.block.Blocks;

public class SoundReactorCategory implements IRecipeCategory<RecipeHolder<SoundReactorRecipe>> {
    public static final int WIDTH = 162;
    public static final int HEIGHT = 64;

    private final IDrawable icon;
    private final Component title;
    protected final ITickTimer timer;

    private final IDrawable arrowIn;
    private final IDrawable arrowOut;

    public SoundReactorCategory(IGuiHelper helper) {
        icon = helper.createDrawableItemStack(AddonBlocks.ANVIL_SOUND_REACTOR.asStack());
        title = Component.translatable("gui.anvilcraft_reverberation.category.sound_reactor");
        timer = helper.createTickTimer(30, 60, true);
        this.arrowIn = JeiRenderHelper.getArrowInput(helper);
        this.arrowOut = JeiRenderHelper.getArrowOutput(helper);
    }

    @Override
    public RecipeType<RecipeHolder<SoundReactorRecipe>> getRecipeType() {
        return AddonJeiPlugin.SOUND_REACTOR;
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<SoundReactorRecipe> recipeHolder, IFocusGroup focuses) {
        SoundReactorRecipe recipe = recipeHolder.value();

        // Get ingredient items from the predicate
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 24).addIngredients(Ingredient.of(recipe.ingredient().getItems()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 24).addItemStack(recipe.result());

        // Add catalyst slot (the sound reactor block)
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST).addItemStack(AddonBlocks.ANVIL_SOUND_REACTOR.asStack());
    }

    @Override
    public void draw(
        RecipeHolder<SoundReactorRecipe> recipeHolder,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY
    ) {
        // Draw Block
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);
        RenderSupport.renderBlock(
            guiGraphics,
            Blocks.ANVIL.defaultBlockState(),
            81,
            22 + anvilYOffset,
            20,
            12,
            RenderSupport.SINGLE_BLOCK);
        RenderSupport.renderBlock(
            guiGraphics,
            AddonBlocks.ANVIL_SOUND_REACTOR.getDefaultState(),
            81,
            40,
            10,
            12,
            RenderSupport.SINGLE_BLOCK);
        RenderSupport.renderBlock(
            guiGraphics,
            AddonBlocks.MERGE_SOUND_PILLAR.getDefaultState(),
            81,
            50,
            0,
            12,
            RenderSupport.SINGLE_BLOCK);

        // Draw arrow
        arrowIn.draw(guiGraphics, 54, 30);
        arrowOut.draw(guiGraphics, 92, 29);

        // Draw sound requirement info
        SoundReactorRecipe recipe = recipeHolder.value();
        guiGraphics.drawString(
            Minecraft.getInstance().font,
            SoundPredicate.getEnergyDescription(recipe.soundPredicate()).getString(),
            1,
            1,
            0xFF000000,
            false
        );
        guiGraphics.drawString(
            Minecraft.getInstance().font,
            SoundPredicate.getSourceNumDescription(recipe.soundPredicate()).getString(),
            1,
            13,
            0xFF000000,
            false
        );
        guiGraphics.drawString(
            Minecraft.getInstance().font,
            SoundPredicate.getTimbreDescription(recipe.soundPredicate()).getString(),
            1,
            45,
            0xFF000000,
            false
        );
        guiGraphics.drawString(
            Minecraft.getInstance().font,
            SoundPredicate.getMelodyDescription(recipe.soundPredicate()).getString(),
            1,
            55,
            0xFF000000,
            false
        );
    }

    public static void registerRecipes(mezz.jei.api.registration.IRecipeRegistration registration) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.getSingleplayerServer() == null) return;
        registration.addRecipes(
            AddonJeiPlugin.SOUND_REACTOR,
            JeiRecipeUtil.getRecipeHoldersFromType(AddonRecipeType.SOUND_REACTOR_TYPE.get())
        );
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(AddonBlocks.ANVIL_SOUND_REACTOR), AddonJeiPlugin.SOUND_REACTOR);
        registration.addRecipeCatalyst(new ItemStack(AddonBlocks.MERGE_SOUND_PILLAR), AddonJeiPlugin.SOUND_REACTOR);
    }
}
