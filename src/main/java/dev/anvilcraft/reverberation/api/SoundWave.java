package dev.anvilcraft.reverberation.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public record SoundWave(int loudness, Timbre timbre, BlockPos pos) {

    public SoundWave(int loudness, Block timbre, BlockPos pos) {
        this(loudness, Timbre.of(timbre), pos);
    }

    public SoundWave copy() {
        return new SoundWave(this.loudness, this.timbre, this.pos);
    }

}
