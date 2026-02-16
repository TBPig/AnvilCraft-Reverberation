package dev.anvilcraft.reverberation.block.entity;

import dev.anvilcraft.reverberation.api.ISoundProducer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ClangCrystalBlockEntity extends BlockEntity implements ISoundProducer {
    public ClangCrystalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
