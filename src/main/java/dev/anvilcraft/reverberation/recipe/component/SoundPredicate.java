package dev.anvilcraft.reverberation.recipe.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.anvilcraft.reverberation.api.Melody;
import dev.anvilcraft.reverberation.api.MergeSound;
import dev.anvilcraft.reverberation.api.MergeSoundStore;
import dev.anvilcraft.reverberation.api.Timbre;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

/**
 * 声音谓词
 * <p>
 * 用于定义声音匹配规则，包括能量范围、源数量范围、音调和旋律
 * </p>
 * @param minEnergy         最小能量
 * @param maxEnergy         最大能量
 * @param minSourceNum    最小源数量
 * @param maxSourceNum    最大源数量
 * @param timbre        音调
 * @param melody        旋律
 */
public record SoundPredicate(
    @Nullable Integer minEnergy,
    @Nullable Integer maxEnergy,
    @Nullable Integer minSourceNum,
    @Nullable Integer maxSourceNum,
    @Nullable Timbre timbre,
    @Nullable Melody melody
) {
    public static final Codec<SoundPredicate> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.INT.optionalFieldOf("minEnergy").forGetter(req -> Optional.ofNullable(req.minEnergy)),
            Codec.INT.optionalFieldOf("maxEnergy").forGetter(req -> Optional.ofNullable(req.maxEnergy)),
            Codec.INT.optionalFieldOf("minSourceNum").forGetter(req -> Optional.ofNullable(req.minSourceNum)),
            Codec.INT.optionalFieldOf("maxSourceNum").forGetter(req -> Optional.ofNullable(req.maxSourceNum)),
            Timbre.CODEC.optionalFieldOf("timbre").forGetter(req -> Optional.ofNullable(req.timbre)),
            Melody.CODEC.optionalFieldOf("melody").forGetter(req -> Optional.ofNullable(req.melody))
        )
        .apply(
            instance, (minEnergy, maxEnergy, minSourceNum, maxSourceNum, requiredTimbres, requiredMelody) -> new SoundPredicate(
                minEnergy.orElse(null),
                maxEnergy.orElse(null),
                minSourceNum.orElse(null),
                maxSourceNum.orElse(null),
                requiredTimbres.orElse(null),
                requiredMelody.orElse(null)
            )
        )
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SoundPredicate> STREAM_CODEC = StreamCodec.of(
        (buf, req) -> {
            writeOptionalInteger(buf, req.minEnergy());
            writeOptionalInteger(buf, req.maxEnergy());
            writeOptionalInteger(buf, req.minSourceNum());
            writeOptionalInteger(buf, req.maxSourceNum());
            writeTimbre(buf, req);
            writeMelody(buf, req);
        },
        (buf) -> {
            Integer minEnergy = readOptionalInteger(buf);
            Integer maxEnergy = readOptionalInteger(buf);
            Integer minSourceNum = readOptionalInteger(buf);
            Integer maxSourceNum = readOptionalInteger(buf);
            Timbre requiredTimbre = readTimbre(buf);
            Melody requiredMelody = readMelody(buf);

            return new SoundPredicate(minEnergy, maxEnergy, minSourceNum, maxSourceNum, requiredTimbre, requiredMelody);
        }
    );

    /**
     * 检查当前的合并音波是否满足此要求
     */
    @SuppressWarnings("RedundantIfStatement")
    public boolean isValid(MergeSoundStore soundStore) {
        if (soundStore.getSoundHistory().isEmpty()) return false;

        MergeSound lastSound = soundStore.getLastSound();
        int currentEnergy = lastSound.getEnergy();
        int currentSourceNum = lastSound.getSourceNum();
        Set<Timbre> currentTimbres = lastSound.getTimbreSet();

        if (minEnergy != null && currentEnergy < minEnergy) {
            return false;
        }
        if (maxEnergy != null && currentEnergy > maxEnergy) {
            return false;
        }
        if (minSourceNum != null && currentSourceNum < minSourceNum) {
            return false;
        }
        if (maxSourceNum != null && currentSourceNum > maxSourceNum) {
            return false;
        }
        if (timbre != null && !currentTimbres.contains(timbre)) {
            return false;
        }
        if (melody != null && !melody.satisfy(soundStore)) {
            return false;
        }

        return true;
    }

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

    private static void writeMelody(RegistryFriendlyByteBuf buf, SoundPredicate req) {
        buf.writeBoolean(req.melody() != null);
        if (req.melody() != null) {
            Melody.STREAM_CODEC.encode(buf, req.melody());
        }
    }

    public static @Nullable Melody readMelody(RegistryFriendlyByteBuf buf) {
        boolean present = buf.readBoolean();
        return present ? Melody.STREAM_CODEC.decode(buf) : null;
    }

    private static void writeTimbre(RegistryFriendlyByteBuf buf, SoundPredicate req) {
        buf.writeBoolean(req.timbre != null);
        if (req.timbre != null) {
            Timbre.STREAM_CODEC.encode(buf, req.timbre());
        }
    }

    public static @Nullable Timbre readTimbre(RegistryFriendlyByteBuf buf) {
        boolean present = buf.readBoolean();
        return present ? Timbre.STREAM_CODEC.decode(buf) : null;
    }

    /**
     * 获取完整的步骤描述（包含所有要求）
     */
    public static Component getFullDescription(SoundPredicate soundPredicate) {
        StringBuilder description = new StringBuilder();

        description.append(getEnergyDescription(soundPredicate).getString());
        if (!description.isEmpty() && description.charAt(description.length() - 1) != ' ') description.append(" | ");
        description.append(getSourceNumDescription(soundPredicate).getString());
        if (!description.isEmpty() && description.charAt(description.length() - 1) != ' ') description.append(" | ");
        description.append(getTimbreDescription(soundPredicate).getString());
        if (!description.isEmpty() && description.charAt(description.length() - 1) != ' ') description.append(" | ");
        description.append(getMelodyDescription(soundPredicate).getString());

        return Component.literal(description.toString());
    }

    public static Component getEnergyDescription(SoundPredicate soundPredicate) {
        if (soundPredicate.minEnergy() != null && soundPredicate.maxEnergy() != null) {
            return Component.translatable(
                "tooltip.anvilcraft_reverberation.sound_require.energy",
                soundPredicate.minEnergy(), soundPredicate.maxEnergy()
            );
        } else if (soundPredicate.minEnergy() != null) {
            return Component.translatable(
                "tooltip.anvilcraft_reverberation.sound_require.min_energy",
                soundPredicate.minEnergy()
            );
        } else if (soundPredicate.maxEnergy() != null) {
            return Component.translatable(
                "tooltip.anvilcraft_reverberation.sound_require.max_energy",
                soundPredicate.maxEnergy()
            );
        }
        return Component.empty();
    }

    public static Component getSourceNumDescription(SoundPredicate soundPredicate) {
        if (soundPredicate.minSourceNum() != null && soundPredicate.maxSourceNum() != null) {
            return Component.translatable(
                "tooltip.anvilcraft_reverberation.sound_require.sources",
                soundPredicate.minSourceNum(), soundPredicate.maxSourceNum()
            );
        } else if (soundPredicate.minSourceNum() != null) {
            return Component.translatable(
                "tooltip.anvilcraft_reverberation.sound_require.min_sources",
                soundPredicate.minSourceNum()
            );
        } else if (soundPredicate.maxSourceNum() != null) {
            return Component.translatable(
                "tooltip.anvilcraft_reverberation.sound_require.max_sources",
                soundPredicate.maxSourceNum()
            );
        }
        return Component.empty();
    }

    public static Component getTimbreDescription(SoundPredicate soundPredicate) {
        if (soundPredicate.timbre() != null) {
            return Component.translatable(
                "tooltip.anvilcraft_reverberation.sound_require.timbre",
                soundPredicate.timbre().block().getName()
            );
        }
        return Component.empty();
    }

    public static Component getMelodyDescription(SoundPredicate soundPredicate) {
        if (soundPredicate.melody() != null) {
            return Component.translatable(
                "tooltip.anvilcraft_reverberation.sound_require.melody",
                soundPredicate.melody().getId().getPath()
            );
        }
        return Component.empty();
    }

    public static final SoundPredicate EMPTY = new SoundPredicate(null, null, null, null, null, null);

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unused")
    public static class Builder {
        private @Nullable Integer minEnergy;
        private @Nullable Integer maxEnergy;
        private @Nullable Integer minSourceNum;
        private @Nullable Integer maxSourceNum;
        private @Nullable Timbre requiredTimbre;
        private @Nullable Melody requiredMelody;

        public Builder minEnergy(Integer min) {
            this.minEnergy = min;
            return this;
        }

        public Builder maxEnergy(Integer max) {
            this.maxEnergy = max;
            return this;
        }

        public Builder energy(Integer min, Integer max) {
            this.minEnergy = min;
            this.maxEnergy = max;
            return this;
        }

        public Builder minSourceNum(Integer min) {
            this.minSourceNum = min;
            return this;
        }

        public Builder maxSourceNum(Integer max) {
            this.maxSourceNum = max;
            return this;
        }

        public Builder sourceNum(Integer min, Integer max) {
            this.minSourceNum = min;
            this.maxSourceNum = max;
            return this;
        }

        public Builder timbre(Timbre timbre) {
            this.requiredTimbre = timbre;
            return this;
        }

        public Builder timbre(Block block) {
            this.requiredTimbre = Timbre.of(block);
            return this;
        }

        public Builder melody(Melody requiredMelody) {
            this.requiredMelody = requiredMelody;
            return this;
        }

        public SoundPredicate build() {
            return new SoundPredicate(minEnergy, maxEnergy, minSourceNum, maxSourceNum, requiredTimbre, requiredMelody);
        }
    }
}
