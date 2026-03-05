package dev.anvilcraft.reverberation.api;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public record Timbre(Block block) {
    public static Timbre of(Block block) {
        return new Timbre(block);
    }

    public boolean satisfy(Timbre other) {
        if (other.block == Blocks.AIR) return true;
        return this.block == other.block;
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
}
