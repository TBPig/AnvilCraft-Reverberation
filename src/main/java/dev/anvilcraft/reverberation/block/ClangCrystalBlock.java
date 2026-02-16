package dev.anvilcraft.reverberation.block;

import dev.anvilcraft.reverberation.api.ISoundReceiver;
import dev.anvilcraft.reverberation.api.SoundWave;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class ClangCrystalBlock extends Block implements IHammerRemovable {
    public static final int SPREAD_RANGE = 6;
    public static final List<BlockPos> SPREAD_OFFSETS =
        BlockPos.betweenClosedStream(-SPREAD_RANGE, 0, -SPREAD_RANGE, SPREAD_RANGE, 0, SPREAD_RANGE)
            .map(BlockPos::immutable)
            .toList();

    public static final VoxelShape SHAPE = Shapes.or(
        box(0, 0, 0, 16, 8, 16),
        box(1, 8, 1, 15, 14, 15),
        box(0, 14, 0, 16, 16, 16)
    );

    public ClangCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public static void hitByAnvil(Level level, BlockPos pos, float fallDistance, Block anvil) {
        int energy = Mth.ceil(fallDistance);
        SoundWave wave = new SoundWave(anvil, energy, pos);
        for (BlockPos offset : SPREAD_OFFSETS) {
            BlockPos selectPos = pos.offset(offset);
            BlockEntity entity = level.getBlockEntity(selectPos);
            if (entity instanceof ISoundReceiver receiver) {
                receiver.receiveSound(wave);
            }
        }
    }
}
