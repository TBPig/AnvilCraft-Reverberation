package dev.anvilcraft.reverberation.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.recipe.component.ItemIngredientPredicate;
import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.api.MergeSoundStore;
import dev.anvilcraft.reverberation.api.SoundRequire;
import dev.anvilcraft.reverberation.component.SoundSequenceData;
import dev.anvilcraft.reverberation.init.AddonDataComponents;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 音序蚀刻配方 - 需要按顺序完成多个砧音催化步骤的配方
 */
public record SoundSequenceReactorRecipe(
    ItemIngredientPredicate ingredient,
    ItemStack intermediate,
    ItemStack result,
    List<SoundRequire> steps,
    int loops,
    int priority
) implements Recipe<RecipeInput> {

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return this.ingredient.test(input.getItem(0));
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
        return AddonRecipeType.SOUND_SEQUENCE_REACTOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AddonRecipeType.SOUND_SEQUENCE_REACTOR_TYPE.get();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Stream<RecipeHolder<SoundSequenceReactorRecipe>> getRecipes(
        Level level,
        ItemStack input,
        MergeSoundStore mergeSoundStore
    ) {
        return level.getRecipeManager()
            .getAllRecipesFor(AddonRecipeType.SOUND_SEQUENCE_REACTOR_TYPE.get())
            .stream()
            .filter(holder -> {
                SoundSequenceReactorRecipe recipe = holder.value();
                // 检查是否匹配中间产物或初始物品
                if (input.has(AddonDataComponents.SOUND_SEQUENCE.get())) {
                    SoundSequenceData data = input.get(AddonDataComponents.SOUND_SEQUENCE.get());
                    return data.id().equals(holder.id()) && recipe.canContinue(data, mergeSoundStore);
                } else {
                    return recipe.ingredient.test(input) && recipe.canStart(mergeSoundStore);
                }
            });
    }

    public static Optional<RecipeHolder<SoundSequenceReactorRecipe>> getRecipe(
        Level level,
        ItemStack input,
        MergeSoundStore mergeSoundStore
    ) {
        return getRecipes(level, input, mergeSoundStore)
            .max(Comparator.comparingInt(r -> r.value().priority()));
    }

    public boolean canStart(MergeSoundStore mergeSoundStore) {
        if (steps.isEmpty()) return false;
        return steps.getFirst().isValid(mergeSoundStore);
    }

    public boolean canContinue(SoundSequenceData data, MergeSoundStore mergeSoundStore) {
        if (data.step() >= steps.size() * loops) return false;
        int currentStepIndex = data.step() % steps.size();
        return steps.get(currentStepIndex).isValid(mergeSoundStore);
    }

    public static ItemStack getOutput(
        RecipeHolder<SoundSequenceReactorRecipe> holder,
        ItemStack input,
        SoundSequenceReactorRecipe recipe
    ) {
        ItemStack output;
        int currentStep = getCurrentStep(input);
        if (currentStep >= recipe.steps().size() * recipe.loops()) {
            // 完成所有步骤，生成最终产物
            return recipe.result().copy();
        } else {
            // 生成带有进度数据的过渡物品
            float progress = (float) currentStep / (recipe.steps().size() * recipe.loops());
            SoundSequenceData newData = new SoundSequenceData(
                holder.id(),
                currentStep,
                progress
            );

            output = recipe.intermediate().copy();
            output.set(AddonDataComponents.SOUND_SEQUENCE.get(), newData);
            return output;
        }
    }

    private static int getCurrentStep(ItemStack input) {
        if (input.has(AddonDataComponents.SOUND_SEQUENCE.get())) {
            SoundSequenceData data = input.get(AddonDataComponents.SOUND_SEQUENCE.get());
            if (data != null) {
                return data.step() + 1;
            }
        }
        return 1;
    }

    public static class Serializer implements RecipeSerializer<SoundSequenceReactorRecipe> {
        private static final MapCodec<SoundSequenceReactorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemIngredientPredicate.CODEC.fieldOf("ingredient").forGetter(SoundSequenceReactorRecipe::ingredient),
            ItemStack.CODEC.fieldOf("intermediate").forGetter(SoundSequenceReactorRecipe::intermediate),
            ItemStack.CODEC.fieldOf("result").forGetter(SoundSequenceReactorRecipe::result),
            SoundRequire.CODEC.listOf().fieldOf("steps").forGetter(SoundSequenceReactorRecipe::steps),
            Codec.INT.fieldOf("loops").orElse(1).forGetter(SoundSequenceReactorRecipe::loops),
            Codec.INT.fieldOf("priority").orElse(0).forGetter(SoundSequenceReactorRecipe::priority)
        ).apply(instance, SoundSequenceReactorRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, SoundSequenceReactorRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ItemIngredientPredicate.STREAM_CODEC.encode(buf, recipe.ingredient());
                ItemStack.STREAM_CODEC.encode(buf, recipe.intermediate());
                ItemStack.STREAM_CODEC.encode(buf, recipe.result());
                writeSteps(buf, recipe);
                buf.writeInt(recipe.priority());
                buf.writeInt(recipe.loops());
            },
            (buf) -> new SoundSequenceReactorRecipe(
                ItemIngredientPredicate.STREAM_CODEC.decode(buf),
                ItemStack.STREAM_CODEC.decode(buf),
                ItemStack.STREAM_CODEC.decode(buf),
                readSteps(buf),
                buf.readInt(),
                buf.readInt()
            )
        );

        private static List<SoundRequire> readSteps(RegistryFriendlyByteBuf buf) {
            int stepCount = buf.readVarInt();
            List<SoundRequire> steps = new ArrayList<>();
            for (int i = 0; i < stepCount; i++) {
                steps.add(SoundRequire.STREAM_CODEC.decode(buf));
            }
            return steps;
        }

        private static void writeSteps(RegistryFriendlyByteBuf buf, SoundSequenceReactorRecipe recipe) {
            buf.writeVarInt(recipe.steps().size());
            for (SoundRequire step : recipe.steps()) {
                SoundRequire.STREAM_CODEC.encode(buf, step);
            }
        }

        @Override
        public MapCodec<SoundSequenceReactorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SoundSequenceReactorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    @SuppressWarnings("unused")
    public static class Builder implements RecipeBuilder {
        private @Nullable ItemIngredientPredicate input;
        private @Nullable ItemStack intermediate;
        private @Nullable ItemStack result;
        private final List<SoundRequire> steps = new ArrayList<>();
        private int loops = 1;
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

        public Builder intermediate(Item ingredient) {
            this.intermediate = ingredient.getDefaultInstance();
            return this;
        }

        public Builder result(Item result, int count) {
            this.result = result.getDefaultInstance();
            this.result.setCount(count);
            return this;
        }

        public Builder result(Item result) {
            return result(result, 1);
        }

        public Builder loops(int loops) {
            this.loops = loops;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder soundRequire(SoundRequire soundRequire) {
            this.steps.add(soundRequire);
            return this;
        }

        @Override
        public Item getResult() {
            return result == null ? Items.AIR : result.getItem();
        }

        @Override
        public void save(RecipeOutput recipeOutput, ResourceLocation id) {
            if (input == null) {
                throw new IllegalArgumentException("Recipe ingredients must not be empty, RecipeId: " + id);
            }
            if (intermediate == null) {
                throw new IllegalArgumentException("Recipe intermediate must not be empty, RecipeId: " + id);
            }
            if (result == null) {
                throw new IllegalArgumentException("Recipe result must not be empty, RecipeId: " + id);
            }
            if (steps.isEmpty()) {
                throw new IllegalArgumentException("Recipe must have at least one step, RecipeId: " + id);
            }

            Advancement.Builder advancement = recipeOutput
                .advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
            criteria.forEach(advancement::addCriterion);

            SoundSequenceReactorRecipe recipe = new SoundSequenceReactorRecipe(
                input,
                intermediate,
                result,
                steps,
                loops,
                priority
            );
            recipeOutput.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
        }

        @Override
        public void save(RecipeOutput output, String id) {
            save(output, AnvilCraftReverberation.of(id).withPrefix("sound_sequence/"));
        }

        @Override
        public void save(RecipeOutput output) {
            save(output, BuiltInRegistries.ITEM.getKey(getResult()).getPath());
        }
    }
}
