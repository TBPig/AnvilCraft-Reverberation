package dev.anvilcraft.reverberation.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record SoundRequire(
    @Nullable Integer minEnergy,
    @Nullable Integer maxEnergy,
    @Nullable Integer minSourceNum,
    @Nullable Integer maxSourceNum,
    List<Timbre> requiredTimbres,
    @Nullable Melody requiredMelody
) {
    public static final Codec<SoundRequire> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.INT.optionalFieldOf("minEnergy").forGetter(req -> Optional.ofNullable(req.minEnergy)),
            Codec.INT.optionalFieldOf("maxEnergy").forGetter(req -> Optional.ofNullable(req.maxEnergy)),
            Codec.INT.optionalFieldOf("minSourceNum").forGetter(req -> Optional.ofNullable(req.minSourceNum)),
            Codec.INT.optionalFieldOf("maxSourceNum").forGetter(req -> Optional.ofNullable(req.maxSourceNum)),
            Timbre.CODEC.listOf().optionalFieldOf("requiredTimbres").forGetter(req -> Optional.ofNullable(req.requiredTimbres)),
            Melody.CODEC.optionalFieldOf("requiredMelody").forGetter(req -> Optional.ofNullable(req.requiredMelody))
        )
        .apply(
            instance, (minEnergy, maxEnergy, minSourceNum, maxSourceNum, requiredTimbres, requiredMelody) -> new SoundRequire(
                minEnergy.orElse(null),
                maxEnergy.orElse(null),
                minSourceNum.orElse(null),
                maxSourceNum.orElse(null),
                requiredTimbres.orElse(new ArrayList<>()),
                requiredMelody.orElse(null)
            )
        )
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SoundRequire> STREAM_CODEC = StreamCodec.of(
        (buf, req) -> {
            writeOptionalInteger(buf, req.minEnergy());
            writeOptionalInteger(buf, req.maxEnergy());
            writeOptionalInteger(buf, req.minSourceNum());
            writeOptionalInteger(buf, req.maxSourceNum());
            writeTimbres(buf, req);
            writeMelody(buf, req);
        },
        (buf) -> {
            Integer minEnergy = readOptionalInteger(buf);
            Integer maxEnergy = readOptionalInteger(buf);
            Integer minSourceNum = readOptionalInteger(buf);
            Integer maxSourceNum = readOptionalInteger(buf);
            List<Timbre> requiredTimbres = readTimbres(buf);
            Melody requiredMelody = readMelody(buf);

            return new SoundRequire(minEnergy, maxEnergy, minSourceNum, maxSourceNum, requiredTimbres, requiredMelody);
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

        for (Timbre requiredTimbre : requiredTimbres) {
            if (!currentTimbres.contains(requiredTimbre)) return false;
        }

        if (requiredMelody != null && !requiredMelody.satisfy(soundStore)) {
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

    private static void writeMelody(RegistryFriendlyByteBuf buf, SoundRequire req) {
        buf.writeBoolean(req.requiredMelody() != null);
        if (req.requiredMelody() != null) {
            Melody.STREAM_CODEC.encode(buf, req.requiredMelody());
        }
    }

    public static @Nullable Melody readMelody(RegistryFriendlyByteBuf buf) {
        boolean present = buf.readBoolean();
        return present ? Melody.STREAM_CODEC.decode(buf) : null;
    }

    private static void writeTimbres(RegistryFriendlyByteBuf buf, SoundRequire req) {
        buf.writeVarInt(req.requiredTimbres().size());
        for (Timbre timbre : req.requiredTimbres()) {
            Timbre.STREAM_CODEC.encode(buf, timbre);
        }
    }

    public static List<Timbre> readTimbres(RegistryFriendlyByteBuf buf) {
        List<Timbre> requiredTimbres = new ArrayList<>();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            requiredTimbres.add(Timbre.STREAM_CODEC.decode(buf));
        }
        return requiredTimbres;
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unused")
    public static class Builder {
        @Nullable
        private Integer minEnergy;
        @Nullable
        private Integer maxEnergy;
        @Nullable
        private Integer minSourceNum;
        @Nullable
        private Integer maxSourceNum;
        private final List<Timbre> requiredTimbres = new ArrayList<>();
        @Nullable
        private Melody requiredMelody;

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
            this.requiredTimbres.add(timbre);
            return this;
        }
        public Builder timbre(Block block) {
            this.requiredTimbres.add(Timbre.of(block));
            return this;
        }

        public Builder melody(Melody requiredMelody) {
            this.requiredMelody = requiredMelody;
            return this;
        }

        public SoundRequire build() {
            return new SoundRequire(minEnergy, maxEnergy, minSourceNum, maxSourceNum, requiredTimbres, requiredMelody);
        }
    }
}
