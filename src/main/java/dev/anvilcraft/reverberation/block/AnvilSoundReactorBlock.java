package dev.anvilcraft.reverberation.block;

import dev.anvilcraft.reverberation.block.entity.MergeSoundPillarBlockEntity;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.anvilcraft.reverberation.recipe.SoundSequenceEtchingRecipe;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Optional;

public class AnvilSoundReactorBlock extends Block implements IHammerRemovable {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape INSIDE = box(2.0, 12.0, 2.0, 14.0, 16.0, 14.0);
    public static final VoxelShape SHAPE = Shapes.join(Shapes.block(), INSIDE, BooleanOp.ONLY_FIRST);

    public AnvilSoundReactorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, facing);
    }

    public static void hitByAnvil(Level level, BlockPos pos, BlockState reactor) {
        if (!(level.getBlockEntity(pos.below()) instanceof MergeSoundPillarBlockEntity pillar)) return;

        Direction direction = reactor.getValue(FACING);
        Vec3 spawnPos = pos.offset(direction.getStepX(), 0, direction.getStepZ())
            .getCenter()
            .add(0, 0.25, 0);
        List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos));

        for (ItemEntity entity : itemEntities) {
            ItemStack entityStack = entity.getItem();

            Optional<RecipeHolder<SoundSequenceEtchingRecipe>> sequenceOpt =
                SoundSequenceEtchingRecipe.getRecipe(level, entityStack, pillar.getSound());

            Optional<RecipeHolder<SoundReactorRecipe>> simpleOpt =
                SoundReactorRecipe.getRecipe(level, entityStack, pillar.getSound());

            int flag;
            if (sequenceOpt.isPresent() && simpleOpt.isEmpty()) {
                flag = 1;
            } else if (sequenceOpt.isEmpty() && simpleOpt.isPresent()) {
                flag = 0;
            } else if (sequenceOpt.isPresent() && simpleOpt.isPresent()) {
                flag = sequenceOpt.get().value().priority() > simpleOpt.get().value().priority() ? 1 : 0;
            } else {
                flag = 2;
            }

            if (flag == 1) {
                // 处理序列配方
                RecipeHolder<SoundSequenceEtchingRecipe> holder = sequenceOpt.get();
                ItemStack output = SoundSequenceEtchingRecipe.getOutput(holder, entityStack, holder.value());
                transItem(level, output, spawnPos, entityStack);
            } else if (flag == 0) {
                // 处理简单配方
                RecipeHolder<SoundReactorRecipe> holder = simpleOpt.get();
                ItemStack output = holder.value().result();
                transItem(level, output, spawnPos, entityStack);
            }
        }
    }

    /**
     * 生成物品实体并处理源物品
     */
    private static void transItem(
        Level level,
        ItemStack output,
        Vec3 spawnPos,
        ItemStack sourceStack
    ) {
        if (output.getMaxStackSize() <= 0) return;

        int count = sourceStack.getCount();
        int consumeCount = 0;
        while (consumeCount < count) {
            int stackSize = Math.min(count - consumeCount, output.getMaxStackSize());
            ItemStack stack = output.copy();
            stack.setCount(stackSize);
            ItemEntity resultEntity = new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, stack, 0, 0, 0);
            level.addFreshEntity(resultEntity);
            consumeCount += stackSize;
        }
        sourceStack.shrink(count);
    }
}
