package dev.anvilcraft.reverberation.block;

import com.mojang.serialization.MapCodec;
import dev.anvilcraft.reverberation.api.SoundWave;
import dev.anvilcraft.reverberation.block.entity.ClangCrystalBlockEntity;
import dev.anvilcraft.reverberation.init.AddonBlockEntities;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ClangCrystalBlock extends BaseEntityBlock implements IHammerRemovable {
    public static final VoxelShape SHAPE = Shapes.or(
        box(0, 0, 0, 16, 8, 16),
        box(1, 8, 1, 15, 14, 15),
        box(0, 16, 0, 16, 16, 16)
    );

    public ClangCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(MergeSoundPillarBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return AddonBlockEntities.CLANG_CRYSTAL.create(blockPos, blockState);
    }

    public void hitByAnvil(Level level, BlockPos pos, float fallDistance, Block anvil) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ClangCrystalBlockEntity clangCrystalBlockEntity) {
            int energy = Mth.ceil(fallDistance);
            clangCrystalBlockEntity.produceSound(new SoundWave(anvil, energy, pos));
        }
    }
}
