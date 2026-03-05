package dev.anvilcraft.reverberation.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.lib.recipe.component.ItemIngredientPredicate;
import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.init.AddonRecipeType;
import lombok.Getter;
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
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class SoundReactorRecipe implements Recipe<RecipeInput> {
    private final ItemIngredientPredicate input;
    private final ItemStack result;
    @Nullable
    private final Integer minEnergy;
    @Nullable
    private final Integer maxEnergy;
    @Nullable
    private final Integer minSourceNum;
    @Nullable
    private final Integer maxSourceNum;
    private final int priority;

    public SoundReactorRecipe(
        ItemIngredientPredicate input,
        ItemStack result,
        @Nullable Integer minEnergy,
        @Nullable Integer maxEnergy,
        @Nullable Integer minSourceNum,
        @Nullable Integer maxSourceNum,
        int priority
    ) {
        this.input = input;
        this.result = result;
        this.minEnergy = minEnergy;
        this.maxEnergy = maxEnergy;
        this.minSourceNum = minSourceNum;
        this.maxSourceNum = maxSourceNum;
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

    /**
     * 配方序列化器
     */
    public static class Serializer implements RecipeSerializer<SoundReactorRecipe> {
        private static void writeOptionalInteger(RegistryFriendlyByteBuf buf, @Nullable Integer value) {
            buf.writeBoolean(value != null);
            if (value != null) {
                buf.writeInt(value);
            }
        }

        private static @Nullable Integer readOptionalInteger(RegistryFriendlyByteBuf buf) {
            boolean present = buf.readBoolean();
            return present ? buf.readInt() : null;
        }

        private static final MapCodec<SoundReactorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemIngredientPredicate.CODEC.fieldOf("ingredient").forGetter(SoundReactorRecipe::getInput),
            ItemStack.CODEC.fieldOf("result").forGetter(SoundReactorRecipe::getResult),
            Codec.INT.optionalFieldOf("minEnergy").forGetter(recipe -> Optional.ofNullable(recipe.minEnergy)),
            Codec.INT.optionalFieldOf("maxEnergy").forGetter(recipe -> Optional.ofNullable(recipe.maxEnergy)),
            Codec.INT.optionalFieldOf("minSourceNum").forGetter(recipe -> Optional.ofNullable(recipe.minSourceNum)),
            Codec.INT.optionalFieldOf("maxSourceNum").forGetter(recipe -> Optional.ofNullable(recipe.maxSourceNum)),
            Codec.INT.fieldOf("priority").orElse(0).forGetter(SoundReactorRecipe::getPriority)
        ).apply(
            instance, (input, result, minEnergyOpt, maxEnergyOpt, minSourceNumOpt, maxSourceNumOpt, priority) ->
                new SoundReactorRecipe(
                    input,
                    result,
                    minEnergyOpt.orElse(null),
                    maxEnergyOpt.orElse(null),
                    minSourceNumOpt.orElse(null),
                    maxSourceNumOpt.orElse(null),
                    priority
                )
        ));

        private static final StreamCodec<RegistryFriendlyByteBuf, SoundReactorRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ItemIngredientPredicate.STREAM_CODEC.encode(buf, recipe.getInput());
                ItemStack.STREAM_CODEC.encode(buf, recipe.getResult());
                writeOptionalInteger(buf, recipe.getMinEnergy());
                writeOptionalInteger(buf, recipe.getMaxEnergy());
                writeOptionalInteger(buf, recipe.getMinSourceNum());
                writeOptionalInteger(buf, recipe.getMaxSourceNum());
                buf.writeInt(recipe.getPriority());
            },
            (buf) -> new SoundReactorRecipe(
                ItemIngredientPredicate.STREAM_CODEC.decode(buf),
                ItemStack.STREAM_CODEC.decode(buf),
                readOptionalInteger(buf),
                readOptionalInteger(buf),
                readOptionalInteger(buf),
                readOptionalInteger(buf),
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
        @Nullable
        private Integer minEnergy;
        @Nullable
        private Integer maxEnergy;
        @Nullable
        private Integer minSourceNum;
        @Nullable
        private Integer maxSourceNum;
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

        public Builder minEnergy(int minEnergy) {
            this.minEnergy = minEnergy;
            return this;
        }

        public Builder maxEnergy(int maxEnergy) {
            this.maxEnergy = maxEnergy;
            return this;
        }

        public Builder minSourceNum(int minSourceNum) {
            this.minSourceNum = minSourceNum;
            return this;
        }

        public Builder maxSourceNum(int maxSourceNum) {
            this.maxSourceNum = maxSourceNum;
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
            if (result == null) {
                throw new IllegalArgumentException("Recipe result must not be empty, RecipeId: " + id);
            }

            Advancement.Builder advancement = recipeOutput
                .advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
            criteria.forEach(advancement::addCriterion);
            SoundReactorRecipe recipe = new SoundReactorRecipe(
                input,
                result,
                minEnergy,
                maxEnergy,
                minSourceNum,
                maxSourceNum,
                priority
            );
            recipeOutput.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
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
