package dev.anvilcraft.reverberation.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public record SoundWave(SoundKind pitch, int loudness, BlockPos pos) {

    public SoundWave(Block anvil, int energy, BlockPos pos) {
        this(SoundKind.getInstance(anvil), energy, pos);
    }

    public boolean satisfy(SoundWave other) {
        return this.pitch.equals(other.pitch) && this.loudness >= other.loudness;
    }

    public SoundWave copy() {
        return new SoundWave(this.pitch, this.loudness, this.pos);
    }

}
