package dev.anvilcraft.reverberation.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SoundReactorRecipeSerializer implements RecipeSerializer<SoundReactorRecipe> {
    @Override
    public MapCodec<SoundReactorRecipe> codec() {
        return null;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SoundReactorRecipe> streamCodec() {
        return null;
    }
}
