package dev.anvilcraft.reverberation.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public record SoundWave(int loudness, Pitch pitch, Block timbre, BlockPos pos) {

    public SoundWave(int loudness, Block anvil, Block timbre, BlockPos pos) {
        this(loudness, Pitch.getInstance(anvil), timbre, pos);
    }

    public boolean satisfy(SoundWave other) {
        if (!this.pitch.equals(other.pitch)) return false;
        if (this.loudness < other.loudness) return false;
        if (other.timbre != Blocks.AIR && this.timbre != other.timbre) return false;
        return true;
    }

    public SoundWave copy() {
        return new SoundWave(this.loudness, this.pitch, this.timbre, this.pos);
    }

}
