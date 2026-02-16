package dev.anvilcraft.reverberation.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ISoundConsumer {


    default void addProducer(BlockPos pos) {
        getProducerPositions().add(pos);
    }

    default void connect(ISoundProducer producer) {
        this.addProducer(producer.getBlockPos());
        producer.addConsumer(this.getBlockPos());
    }

    default void consumeSound(SoundWave soundWave) {
    }

    Set<BlockPos> getProducerPositions();

    BlockPos getBlockPos();

    @Nullable Level getLevel();
}
