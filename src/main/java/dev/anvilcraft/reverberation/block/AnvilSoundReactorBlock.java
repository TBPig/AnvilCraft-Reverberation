package dev.anvilcraft.reverberation.block;

import dev.anvilcraft.reverberation.block.entity.MergeSoundPillarBlockEntity;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnvilSoundReactorBlock extends Block implements IHammerRemovable {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape INSIDE = Shapes.or(
        box(2.0, 12.0, 2.0, 14.0, 16.0, 14.0),
        box(6, 13, 14, 10, 16, 16)
    );
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

        Map<Item, List<ItemEntity>> itemGroups = selectItemEntity(level, pos);
        if (itemGroups == null) return;

        Direction direction = reactor.getValue(FACING);
        Vec3 spawnPos = pos.offset(direction.getStepX(), 0, direction.getStepZ())
            .getCenter()
            .add(0, 0.25, 0);

        // 检查每个物品组
        for (Map.Entry<Item, List<ItemEntity>> entry : itemGroups.entrySet()) {
            Item item = entry.getKey();
            List<ItemEntity> itemEntities = entry.getValue();

            // 计算总数量
            int totalCount = 0;
            for (ItemEntity entity : itemEntities) {
                totalCount += entity.getItem().getCount();
            }

            // 检测是否有对应配方
            ItemStack testStack = new ItemStack(item);
            testStack.setCount(Math.min(testStack.getMaxStackSize(), totalCount));
            Optional<SoundReactorRecipe> optional = SoundReactorRecipe.getRecipe(level, testStack, pillar.getSound());
            if (optional.isEmpty()) continue;
            SoundReactorRecipe recipe = optional.get();

            // 消耗物品
            int oneRequiredCount = recipe.input().count();
            int runTime = totalCount / oneRequiredCount;
            translateItem(level, oneRequiredCount, runTime, itemEntities, recipe, spawnPos);
        }
    }

    /**
     * 收集所有物品并按物品类型分组
     */
    private static @Nullable Map<Item, List<ItemEntity>> selectItemEntity(Level level, BlockPos pos) {

        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos));
        if (entities.isEmpty()) return null;

        Map<Item, List<ItemEntity>> itemGroups = new HashMap<>();
        for (ItemEntity itemEntity : entities) {
            itemGroups.computeIfAbsent(itemEntity.getItem().getItem(), k -> new ArrayList<>()).add(itemEntity);
        }
        return itemGroups;
    }

    public static void translateItem(
        Level level,
        int requiredCount,
        int runTime,
        List<ItemEntity> itemEntities,
        SoundReactorRecipe recipe,
        Vec3 spawnPos
    ) {
        int remainingCount = requiredCount * runTime;
        for (ItemEntity entity : itemEntities) {
            if (remainingCount <= 0) break;

            ItemStack entityStack = entity.getItem();
            int consumeCount = Math.min(entityStack.getCount(), remainingCount);
            entityStack.shrink(consumeCount);
            remainingCount -= consumeCount;
        }

        // 生成物品
        ItemStack result = recipe.result();
        for (int i = 0; i < runTime; i++) {
            ItemEntity resultEntity = new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, result.copy(), 0, 0, 0);
            level.addFreshEntity(resultEntity);
        }
    }
}
