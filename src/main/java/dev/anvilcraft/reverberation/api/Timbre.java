package dev.anvilcraft.reverberation.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public record Timbre(Block block) {
    public static final StreamCodec<RegistryFriendlyByteBuf, Timbre> STREAM_CODEC;
    public static final Codec<Timbre> CODEC;

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

    static {
        CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            ResourceLocation.CODEC.xmap(BuiltInRegistries.BLOCK::get, BuiltInRegistries.BLOCK::getKey)
                .fieldOf("block")
                .forGetter(Timbre::block)
        ).apply(builder, Timbre::new));

        STREAM_CODEC = StreamCodec.of(
            (buf, timbre) -> ResourceLocation.STREAM_CODEC.encode(buf, BuiltInRegistries.BLOCK.getKey(timbre.block)),
            (buf) -> new Timbre(BuiltInRegistries.BLOCK.get(ResourceLocation.STREAM_CODEC.decode(buf)))
        );
    }
}
