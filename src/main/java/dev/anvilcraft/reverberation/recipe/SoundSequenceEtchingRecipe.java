package dev.anvilcraft.reverberation.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.recipe.component.ItemIngredientPredicate;
import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.api.MergeSoundStore;
import dev.anvilcraft.reverberation.recipe.component.SoundPredicate;
import dev.anvilcraft.reverberation.component.SoundSequenceData;
import dev.anvilcraft.reverberation.init.AddonDataComponents;
import dev.anvilcraft.reverberation.init.AddonRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 音序蚀刻配方 - 需要按顺序完成多个砧音催化步骤的配方
 */
public record SoundSequenceEtchingRecipe(
    ItemIngredientPredicate ingredient,
    ItemStack intermediate,
    ItemStack result,
    List<SoundPredicate> steps,
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
        return AddonRecipeType.SOUND_SEQUENCE_ETCHING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AddonRecipeType.SOUND_SEQUENCE_ETCHING_TYPE.get();
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings({"DataFlowIssue"})
    public static Optional<RecipeHolder<SoundSequenceEtchingRecipe>> getRecipe(
        Level level,
        ItemStack input,
        MergeSoundStore mergeSoundStore
    ) {
        return level.getRecipeManager()
            .getAllRecipesFor(AddonRecipeType.SOUND_SEQUENCE_ETCHING_TYPE.get())
            .stream()
            .filter(holder -> {
                SoundSequenceEtchingRecipe recipe = holder.value();
                // 检查是否匹配中间产物或初始物品
                if (input.has(AddonDataComponents.SOUND_SEQUENCE.get())) {
                    SoundSequenceData data = input.get(AddonDataComponents.SOUND_SEQUENCE.get());
                    return data.id().equals(holder.id()) && recipe.canContinue(data, mergeSoundStore);
                } else {
                    return recipe.ingredient.test(input) && recipe.canStart(mergeSoundStore);
                }
            })
            .max(Comparator.comparingInt(r -> r.value().priority()));
    }

    @SuppressWarnings({"DataFlowIssue"})
    public static Optional<RecipeHolder<SoundSequenceEtchingRecipe>> getRecipe(
        Level level,
        ItemStack input
    ) {
        return level.getRecipeManager()
            .getAllRecipesFor(AddonRecipeType.SOUND_SEQUENCE_ETCHING_TYPE.get())
            .stream()
            .filter(holder -> {
                if (input.has(AddonDataComponents.SOUND_SEQUENCE.get())) {
                    SoundSequenceData data = input.get(AddonDataComponents.SOUND_SEQUENCE.get());
                    return data.id().equals(holder.id());
                } else {
                    return false;
                }
            })
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
        RecipeHolder<SoundSequenceEtchingRecipe> holder,
        ItemStack input,
        SoundSequenceEtchingRecipe recipe
    ) {
        ItemStack output;
        int currentStep = getCurrentStep(input);
        if (currentStep >= recipe.allStepNum()) {
            // 完成所有步骤，生成最终产物
            return recipe.result().copy();
        } else {
            // 生成带有进度数据的过渡物品
            float progress = (float) currentStep / recipe.allStepNum();
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

    public int allStepNum() {
        return steps.size() * loops();
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


    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings({"DataFlowIssue"})
    public static void addToTooltip(ItemTooltipEvent event) {
        // 源代码参考自《机械动力》：https://github.com/Creators-of-Create/Create
        ItemStack stack = event.getItemStack();
        if (!stack.has(AddonDataComponents.SOUND_SEQUENCE.get())) {
            return;
        }

        SoundSequenceData soundSequenceData = stack.get(AddonDataComponents.SOUND_SEQUENCE.get());
        Optional<RecipeHolder<SoundSequenceEtchingRecipe>> optionalRecipe = getRecipe(Minecraft.getInstance().level, stack);

        if (optionalRecipe.isEmpty()) return;

        SoundSequenceEtchingRecipe recipe = optionalRecipe.get().value();

        int length = recipe.steps().size();
        int step = soundSequenceData.step();
        int total = recipe.allStepNum();
        List<Component> tooltip = event.getToolTip();

        tooltip.add(Component.translatable("tooltip.anvilcraft_reverberation.sound_sequence.progress")
            .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.anvilcraft_reverberation.sound_sequence.step", step + 1, total)
            .withStyle(ChatFormatting.DARK_GRAY));

        int remaining = total - step;
        for (int i = 0; i < length; i++) {
            if (i >= remaining) {
                break;
            }
            SoundPredicate soundPredicate = recipe.steps().get((i + step) % length);
            Component textComponent = SoundPredicate.getFullDescription(soundPredicate);
            if (i == 0) {
                tooltip.add(Component.translatable("tooltip.anvilcraft_reverberation.sound_sequence.next", textComponent)
                    .withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.literal("→ ").append(textComponent)
                    .withStyle(ChatFormatting.DARK_AQUA));
            }
        }
    }

    public static class Serializer implements RecipeSerializer<SoundSequenceEtchingRecipe> {
        private static final MapCodec<SoundSequenceEtchingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemIngredientPredicate.CODEC.fieldOf("ingredient").forGetter(SoundSequenceEtchingRecipe::ingredient),
            ItemStack.CODEC.fieldOf("intermediate").forGetter(SoundSequenceEtchingRecipe::intermediate),
            ItemStack.CODEC.fieldOf("result").forGetter(SoundSequenceEtchingRecipe::result),
            SoundPredicate.CODEC.listOf().fieldOf("steps").forGetter(SoundSequenceEtchingRecipe::steps),
            Codec.INT.fieldOf("loops").orElse(1).forGetter(SoundSequenceEtchingRecipe::loops),
            Codec.INT.fieldOf("priority").orElse(0).forGetter(SoundSequenceEtchingRecipe::priority)
        ).apply(instance, SoundSequenceEtchingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, SoundSequenceEtchingRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ItemIngredientPredicate.STREAM_CODEC.encode(buf, recipe.ingredient());
                ItemStack.STREAM_CODEC.encode(buf, recipe.intermediate());
                ItemStack.STREAM_CODEC.encode(buf, recipe.result());
                writeSteps(buf, recipe);
                buf.writeInt(recipe.loops());
                buf.writeInt(recipe.priority());
            },
            (buf) -> new SoundSequenceEtchingRecipe(
                ItemIngredientPredicate.STREAM_CODEC.decode(buf),
                ItemStack.STREAM_CODEC.decode(buf),
                ItemStack.STREAM_CODEC.decode(buf),
                readSteps(buf),
                buf.readInt(),
                buf.readInt()
            )
        );

        private static List<SoundPredicate> readSteps(RegistryFriendlyByteBuf buf) {
            int stepCount = buf.readVarInt();
            List<SoundPredicate> steps = new ArrayList<>();
            for (int i = 0; i < stepCount; i++) {
                steps.add(SoundPredicate.STREAM_CODEC.decode(buf));
            }
            return steps;
        }

        private static void writeSteps(RegistryFriendlyByteBuf buf, SoundSequenceEtchingRecipe recipe) {
            buf.writeVarInt(recipe.steps().size());
            for (SoundPredicate step : recipe.steps()) {
                SoundPredicate.STREAM_CODEC.encode(buf, step);
            }
        }

        @Override
        public MapCodec<SoundSequenceEtchingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SoundSequenceEtchingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    @SuppressWarnings("unused")
    public static class Builder implements RecipeBuilder {
        private @Nullable ItemIngredientPredicate input;
        private @Nullable ItemStack intermediate;
        private @Nullable ItemStack result;
        private final List<SoundPredicate> steps = new ArrayList<>();
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

        public Builder soundRequire(SoundPredicate soundPredicate) {
            this.steps.add(soundPredicate);
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

            SoundSequenceEtchingRecipe recipe = new SoundSequenceEtchingRecipe(
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
