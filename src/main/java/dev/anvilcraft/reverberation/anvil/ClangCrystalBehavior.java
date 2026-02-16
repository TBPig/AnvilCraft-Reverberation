package dev.anvilcraft.reverberation.anvil;

import dev.anvilcraft.reverberation.block.ClangCrystalBlock;
import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ClangCrystalBehavior implements IAnvilBehavior {
    @Override
    public boolean handle(Level level, BlockPos hitBlockPos, BlockState hitBlockState, float fallDistance, AnvilEvent.OnLand event) {
        ClangCrystalBlock block = (ClangCrystalBlock) hitBlockState.getBlock();
        Block anvil = event.getEntity().getBlockState().getBlock();
        block.hitByAnvil(level, hitBlockPos, fallDistance, anvil);
        return false;
    }
}
