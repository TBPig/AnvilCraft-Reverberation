package dev.anvilcraft.reverberation.block;

import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClangCrystal extends Block implements IHammerRemovable {
    public static final VoxelShape SHAPE = Shapes.or(
        box(0, 0, 0, 16, 8, 16),
        box(1, 8, 1, 15, 14, 15),
        box(0, 16, 0, 16, 16, 16)
    );

    public ClangCrystal(Properties properties) {
        super(properties);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
