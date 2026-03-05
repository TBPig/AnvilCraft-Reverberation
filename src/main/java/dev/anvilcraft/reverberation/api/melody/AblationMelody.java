package dev.anvilcraft.reverberation.api.melody;

import dev.anvilcraft.reverberation.api.MergeSound;
import dev.anvilcraft.reverberation.api.MergeSoundStore;
import dev.anvilcraft.reverberation.api.SoundWave;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class AblationMelody extends Melody {
    @Override
    public boolean satisfy(MergeSoundStore mergeSoundStore) {
        List<MergeSound> soundHistory = mergeSoundStore.getSoundHistory();
        if (soundHistory.isEmpty()) return false;

        MergeSound sound = soundHistory.getLast();
        List<SoundWave> soundWaves = sound.getSoundWaves();
        int frostEnergy = 0;
        int emberEnergy = 0;
        for (SoundWave soundWave : soundWaves) {
            Block block = soundWave.timbre().block();
            // TODO: 根据需要定义霜冻和余烬对应的方块
            // if (block == ModBlocks.FROST_ANVIL) {
            //     frostEnergy += soundWave.loudness();
            // } else if (block == ModBlocks.EMBER_ANVIL) {
            //     emberEnergy += soundWave.loudness();
            // }
        }
        return frostEnergy == emberEnergy;
    }
}
