package dev.anvilcraft.reverberation.component;

import net.minecraft.resources.ResourceLocation;

/**
 * 有序组装数据结构
 */
public record SoundSequenceData(
    ResourceLocation id,
    int step,
    float progress
) {
    public static final com.mojang.serialization.Codec<SoundSequenceData> CODEC =
        com.mojang.serialization.codecs.RecordCodecBuilder.create(builder ->
            builder.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(SoundSequenceData::id),
                com.mojang.serialization.Codec.INT.fieldOf("step").forGetter(SoundSequenceData::step),
                com.mojang.serialization.Codec.FLOAT.fieldOf("progress").forGetter(SoundSequenceData::progress)
            ).apply(builder, SoundSequenceData::new)
        );

    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, SoundSequenceData> STREAM_CODEC =
        net.minecraft.network.codec.StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            SoundSequenceData::id,
            net.minecraft.network.codec.ByteBufCodecs.INT,
            SoundSequenceData::step,
            net.minecraft.network.codec.ByteBufCodecs.FLOAT,
            SoundSequenceData::progress,
            SoundSequenceData::new
        );
}
