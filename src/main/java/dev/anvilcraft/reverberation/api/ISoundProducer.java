package dev.anvilcraft.reverberation.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ISoundProducer {
    Set<BlockPos> consumerPositions = Set.of();

    default void addConsumer(BlockPos pos) {
        consumerPositions.add(pos);
    }

    default void connect(ISoundConsumer consumer) {
        this.addConsumer(consumer.getBlockPos());
        consumer.addProducer(this.getBlockPos());
    }

    default void produceSound(SoundWave soundWave) {
        if (this.getLevel() == null) return;
        consumerPositions.forEach(pos -> {
            BlockEntity blockEntity = this.getLevel().getBlockEntity(pos);
            if (blockEntity instanceof ISoundConsumer consumer) {
                consumer.consumeSound(soundWave);
            } else {
                consumerPositions.remove(pos);
            }
        });
    }

    BlockPos getBlockPos();

    @Nullable Level getLevel();
}
