package dev.anvilcraft.reverberation.api;

import com.google.common.base.Supplier;
import com.mojang.serialization.Codec;
import dev.anvilcraft.reverberation.block.entity.MergeSoundPillarBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Melody {
    private static final Map<ResourceLocation, Melody> MELODY_REGISTRY = new ConcurrentHashMap<>();

    public static final Codec<Melody> CODEC = ResourceLocation.CODEC.xmap(
        Melody::getOrThrow,
        Melody::getId
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Melody> STREAM_CODEC = StreamCodec.of(
        (buf, melody) -> ResourceLocation.STREAM_CODEC.encode(buf, melody.getId()),
        (buf) -> Melody.getOrThrow(ResourceLocation.STREAM_CODEC.decode(buf))
    );

    public static <T extends Melody> T register(Supplier<T> f) {
        T melody = f.get();
        MELODY_REGISTRY.put(melody.getId(), melody);
        return melody;
    }

    public static Melody getOrThrow(ResourceLocation id) {
        Melody melody = MELODY_REGISTRY.get(id);
        if (melody == null) {
            throw new IllegalArgumentException("Unknown Melody: " + id);
        }
        return melody;
    }

    public boolean satisfy(MergeSoundPillarBlockEntity mergeSoundPillarBlockEntity) {
        return satisfy(mergeSoundPillarBlockEntity.getSound());
    }

    public abstract boolean satisfy(MergeSoundStore mergeSoundStore);

    public abstract ResourceLocation getId();
}
