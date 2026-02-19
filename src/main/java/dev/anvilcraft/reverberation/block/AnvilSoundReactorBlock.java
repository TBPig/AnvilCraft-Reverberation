package dev.anvilcraft.reverberation.block;

import dev.anvilcraft.reverberation.block.entity.MergeSoundPillarBlockEntity;
import dev.anvilcraft.reverberation.init.AddonRecipeType;
import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
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

    public static final VoxelShape INSIDE = box(2.0, 2.0, 2.0, 14.0, 16.0, 14.0);
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
        // 搜索本格的ItemEntity，获取ItemStack
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos));
        if (entities.isEmpty()) return;

        Direction direction = reactor.getValue(FACING);
        Vec3 spawnPos = pos.offset(direction.getStepX(), 0, direction.getStepZ()).getCenter();

        for (ItemEntity itemEntity : entities) {
            // 检测是否有对应配方
            ItemStack itemStack = itemEntity.getItem();
            SingleRecipeInput input = new SingleRecipeInput(itemStack);
            Optional<RecipeHolder<SoundReactorRecipe>> recipes = level.getRecipeManager()
                .getRecipeFor(AddonRecipeType.SOUND_REACTOR_TYPE.get(), input, level);
            if (recipes.isEmpty()) continue;
            SoundReactorRecipe recipe = recipes.get().value();

            // 条件判定
            if (!(level.getBlockEntity(pos.below()) instanceof MergeSoundPillarBlockEntity pillar)) continue;
            if (!pillar.isValid(recipe)) continue;

            // 消耗物品
            int count = recipe.ingredient().count();
            if (itemEntity.getItem().getCount() < count) continue;
            itemEntity.getItem().shrink(count);

            // 生成物品
            ItemStack result = recipe.result();
            ItemEntity resultEntity = new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, result.copy(), 0, 0, 0);
            level.addFreshEntity(resultEntity);
        }
    }
}
