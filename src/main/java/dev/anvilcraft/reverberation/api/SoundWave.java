package dev.anvilcraft.reverberation.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public record SoundWave(SoundKind kind, int energy, BlockPos pos) {

    public SoundWave(Block anvil, int energy, BlockPos pos) {
        this(SoundKind.getInstance(anvil), energy, pos);
    }

    public boolean equals(SoundWave other) {
        return this.kind.equals(other.kind) && this.energy == other.energy;
    }

    public SoundWave copy() {
        return new SoundWave(this.kind, this.energy, this.pos);
    }

}
