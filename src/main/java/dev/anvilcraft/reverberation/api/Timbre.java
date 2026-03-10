package dev.anvilcraft.reverberation.api;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public record Timbre(Block block) {
    public static final Codec<Timbre> CODEC = ResourceLocation.CODEC.xmap(
        Timbre::getOrThrow,
        Timbre::getId
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Timbre> STREAM_CODEC = StreamCodec.of(
        (buf, timbre) -> ResourceLocation.STREAM_CODEC.encode(buf, timbre.getId()),
        (buf) -> Timbre.getOrThrow(ResourceLocation.STREAM_CODEC.decode(buf))
    );

    public static Timbre of(Block block) {
        return new Timbre(block);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Timbre(Block b))) return false;
        return this.block.equals(b);
    }

    @Override
    public int hashCode() {
        return this.block.hashCode();
    }

    public static Timbre getOrThrow(ResourceLocation id) {
        Block block = BuiltInRegistries.BLOCK.get(id);
        return new Timbre(block);
    }

    public ResourceLocation getId() {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
}
