package dev.anvilcraft.reverberation.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.recipe.component.ChanceItemStack;
import dev.anvilcraft.lib.recipe.component.ItemIngredientPredicate;
import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.init.AddonRecipeType;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SoundReactorRecipe implements Recipe<RecipeInput> {
    private final ItemIngredientPredicate input;
    private final ItemStack result;
    private final int priority;

    public SoundReactorRecipe(
        ItemIngredientPredicate input,
        ItemStack result,
        int priority
    ) {
        this.input = input;
        this.result = result;
        this.priority = priority;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return this.input.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AddonRecipeType.SOUND_REACTOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AddonRecipeType.SOUND_REACTOR_TYPE.get();
    }

    public static Builder builder() {
        return new Builder();
    }

    public ItemIngredientPredicate ingredient() {
        return input;
    }

    public ItemStack result() {
        return result;
    }

    public int priority() {
        return priority;
    }

    /**
     * 配方序列化器
     */
    public static class Serializer implements RecipeSerializer<SoundReactorRecipe> {
        private static final MapCodec<SoundReactorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemIngredientPredicate.CODEC.fieldOf("ingredient").forGetter(SoundReactorRecipe::ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(SoundReactorRecipe::result),
            Codec.INT.fieldOf("priority").orElse(1).forGetter(SoundReactorRecipe::priority)
        ).apply(instance, SoundReactorRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, SoundReactorRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ItemIngredientPredicate.STREAM_CODEC.encode(buf, recipe.ingredient());
                ItemStack.STREAM_CODEC.encode(buf, recipe.result());
                buf.writeInt(recipe.priority());
            },
            (buf) -> new SoundReactorRecipe(
                ItemIngredientPredicate.STREAM_CODEC.decode(buf),
                ItemStack.STREAM_CODEC.decode(buf),
                buf.readInt()
            )
        );

        @Override
        public MapCodec<SoundReactorRecipe> codec() {
            return Serializer.CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SoundReactorRecipe> streamCodec() {
            return Serializer.STREAM_CODEC;
        }
    }

    public static class Builder implements RecipeBuilder {
        private @Nullable ItemIngredientPredicate input;
        private @Nullable ItemStack result;
        private int priority = 0;
        protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        @Override
        public RecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
            criteria.put(s, criterion);
            return this;
        }

        @Override
        public RecipeBuilder group(@Nullable String s) {
            return this;
        }

        public Builder requires(TagKey<Item> ingredient, int count) {
            this.input = ItemIngredientPredicate.Builder.item().of(ingredient).withCount(count).build();
            return this;
        }

        public Builder requires(TagKey<Item> ingredient) {
            return requires(ingredient, 1);
        }

        public Builder requires(ItemLike ingredient, int count) {
            this.input = ItemIngredientPredicate.Builder.item().of(ingredient).withCount(count).build();
            return this;
        }

        public Builder requires(ItemLike ingredient) {
            return requires(ingredient, 1);
        }

        public Builder result(Item result, int count) {
            this.result = result.getDefaultInstance();
            this.result.setCount(count);
            return this;
        }
        public Builder result(Item result) {
            return result(result, 1);
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        @Override
        public Item getResult() {
            return result == null ? Items.AIR : result.getItem();
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation id) {
            if (input == null) {
                throw new IllegalArgumentException("Recipe ingredients must not be empty, RecipeId: " + id);
            }
            if (result == null) {
                throw new IllegalArgumentException("Recipe result must not be empty, RecipeId: " + id);
            }

            Advancement.Builder advancement = output
                .advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
            criteria.forEach(advancement::addCriterion);
            SoundReactorRecipe recipe = new SoundReactorRecipe(
                input,
                result,
                priority
            );
            output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
        }

        @Override
        public void save(RecipeOutput output, String id) {
            save(output, AnvilCraftReverberation.of(id).withPrefix("sound_reactor/"));
        }

        @Override
        public void save(RecipeOutput output) {
            save(output, BuiltInRegistries.ITEM.getKey(getResult()).getPath());
        }
    }
}
