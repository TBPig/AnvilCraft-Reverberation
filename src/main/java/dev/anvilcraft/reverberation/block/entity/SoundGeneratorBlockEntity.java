package dev.anvilcraft.reverberation.block.entity;

import dev.anvilcraft.reverberation.api.MergeSound;
import dev.anvilcraft.reverberation.block.SoundGeneratorBlock;
import dev.anvilcraft.reverberation.init.AddonBlockEntities;
import dev.dubhe.anvilcraft.api.power.IPowerProducer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SoundGeneratorBlockEntity extends BlockEntity implements IPowerProducer {
    public static final int MAX_POWER = 256;
    public static final int POWER_PER_ENERGY = 1;

    @Setter
    @Getter
    private PowerGrid grid = null;
    private int power = 0;
    private int time = 0;

    public SoundGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(AddonBlockEntities.SOUND_GENERATOR.get(), pos, blockState);
    }

    public static SoundGeneratorBlockEntity createBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        return new SoundGeneratorBlockEntity(pos, blockState);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getInt("power");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("power", this.power);
    }

    @Override
    public void gridTick() {
        if (level == null || level.isClientSide()) return;
        
        int prePower = power;
        
        // Check if there is a MergeSoundPillar below
        BlockPos belowPos = getBlockPos().below();
        BlockEntity belowEntity = level.getBlockEntity(belowPos);
        
        if (belowEntity instanceof MergeSoundPillarBlockEntity pillarEntity) {
            MergeSound mergeSound = pillarEntity.getMergeSound();
            int energy = mergeSound.getEnergy();
            power = Math.min(energy * POWER_PER_ENERGY, MAX_POWER);
        } else {
            power = 0;
        }

        
        if (power != prePower && grid != null) {
            grid.markChanged();
        }
    }

    public void tick() {
        this.time ++;
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return level;
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public int getOutputPower() {
        return power;
    }

    @Override
    public int getTime() {
        return time;
    }
}
