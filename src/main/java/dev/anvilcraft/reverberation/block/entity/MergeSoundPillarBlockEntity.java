package dev.anvilcraft.reverberation.block.entity;

import dev.anvilcraft.reverberation.api.ISoundConsumer;
import dev.anvilcraft.reverberation.api.ISoundProducer;
import dev.anvilcraft.reverberation.api.MergeSound;
import dev.anvilcraft.reverberation.api.SoundWave;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;

public class MergeSoundPillarBlockEntity extends BlockEntity implements ISoundConsumer {
    public static final int PERIOD = 20;
    public static final int SELECTION_COOLDOWN = 200;
    private final List<BlockPos> SELECTION_POSITIONS =
        BlockPos.betweenClosedStream(-7, 0, -7, 7, 0, 7)
            .map(BlockPos::immutable)
            .map((blockPos) -> this.getBlockPos().offset(blockPos))
            .toList();

    private MergeSound newMergeSound;
    private MergeSound lastMergeSound;
    private int tickCount = 0;
    private int selectionTime = 0;
    private Set<BlockPos> producerPositions = Set.of();

    public MergeSoundPillarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        newMergeSound = new MergeSound();
    }

    public void tick() {
        if (level == null) return;
        if (--tickCount <= 0) {
            tickCount = PERIOD;
            lastMergeSound = newMergeSound;
            newMergeSound = new MergeSound();
        }
        if (--selectionTime <= 0) {
            selectionTime = SELECTION_COOLDOWN + level.random.nextInt(SELECTION_COOLDOWN);
            this.select();
        }
    }

    @Override
    public void consumeSound(SoundWave soundWave) {
        this.newMergeSound.add(soundWave);
    }

    @Override
    public Set<BlockPos> getProducerPositions() {
        return producerPositions;
    }

    public MergeSound getMergeSound() {
        return lastMergeSound;
    }

    private void select() {
        if (level == null) return;
        SELECTION_POSITIONS.forEach((blockPos) -> {
            if (level.getBlockEntity(blockPos) instanceof ISoundProducer soundProducer) {
                connect(soundProducer);
            }
        });
    }
}
