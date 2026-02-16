package dev.anvilcraft.reverberation.api;

import dev.dubhe.anvilcraft.init.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public enum SoundKind {
    DEFAULT,
    ROYAL,
    EMBER,
    FROST,
    TRANSCENDENCE;

    final static Map<Block, SoundKind> anvilToKind = new HashMap<>();

    static {
        anvilToKind.put(ModBlocks.ROYAL_ANVIL.get(), ROYAL);
        anvilToKind.put(ModBlocks.EMBER_ANVIL.get(), EMBER);
        anvilToKind.put(ModBlocks.TRANSCENDENCE_ANVIL.get(), TRANSCENDENCE);
    }

    public static SoundKind getInstance(Block block) {
        return anvilToKind.getOrDefault(block, DEFAULT);
    }

    public static SoundKind getInstance(BlockState blockState) {
        return anvilToKind.getOrDefault(blockState.getBlock(), DEFAULT);
    }

    public boolean equals(SoundKind other) {
        if (this == DEFAULT || other == DEFAULT) return true;
        return this == other;
    }
}
