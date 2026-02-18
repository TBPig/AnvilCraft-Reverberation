package dev.anvilcraft.reverberation.anvil;

import dev.anvilcraft.reverberation.block.AnvilSoundReactorBlock;
import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SoundReactorBehavior implements IAnvilBehavior {
    @Override
    public boolean handle(Level level, BlockPos hitBlockPos, BlockState hitBlockState, float fallDistance, AnvilEvent.OnLand event) {
        AnvilSoundReactorBlock.hitByAnvil(level, hitBlockPos, hitBlockState);
        return true;
    }
}
