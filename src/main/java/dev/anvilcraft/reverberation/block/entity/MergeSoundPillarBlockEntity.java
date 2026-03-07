package dev.anvilcraft.reverberation.block.entity;

import dev.anvilcraft.reverberation.api.ISoundReceiver;
import dev.anvilcraft.reverberation.api.MergeSound;
import dev.anvilcraft.reverberation.api.MergeSoundStore;
import dev.anvilcraft.reverberation.api.SoundWave;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MergeSoundPillarBlockEntity extends BlockEntity implements ISoundReceiver {
    public static final int PERIOD = 20;
    public static final int RECEIVE_RANGE = 6;

    @Getter
    private final MergeSoundStore sound;
    private int tickCount = 0;

    public MergeSoundPillarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        sound = new MergeSoundStore();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("tickCount", tickCount);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        tickCount = tag.getInt("tickCount");
    }

    public void tick() {
        if (level == null) return;
        if (--tickCount <= 0) {
            tickCount = PERIOD;
            sound.store();
        }
    }

    @Override
    public boolean receiveSound(SoundWave soundWave) {
        if (getBlockPos().distManhattan(soundWave.pos()) > RECEIVE_RANGE) return false;
        this.sound.add(soundWave);
        return true;
    }

    public MergeSound getMergeSound() {
        return sound.getLastSound();
    }

    public int getAnalogOutputSignal() {
        return tickCount == PERIOD ? 15 : 0;
    }
}
