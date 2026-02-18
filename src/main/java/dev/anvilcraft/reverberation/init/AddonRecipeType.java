package dev.anvilcraft.reverberation.init;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AddonRecipeType {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
        DeferredRegister.create(Registries.RECIPE_TYPE, AnvilCraftReverberation.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, AnvilCraftReverberation.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SoundReactorRecipe>> SOUND_REACTOR_TYPE =
        registerType("sound_reactor_recipe");
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SoundReactorRecipe>> SOUND_REACTOR_SERIALIZER =
        RECIPE_SERIALIZERS.register("sound_reactor_recipe", SoundReactorRecipe.Serializer::new);

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> registerType(String name) {
        return RECIPE_TYPES.register(
            name, () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return AnvilCraftReverberation.of(name).toString();
                }
            }
        );
    }

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
}
