package dev.anvilcraft.reverberation.melody;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.api.Melody;
import dev.anvilcraft.reverberation.api.MergeSound;
import dev.anvilcraft.reverberation.api.MergeSoundStore;
import dev.anvilcraft.reverberation.api.SoundWave;
import dev.dubhe.anvilcraft.init.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
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
             if (block.equals(ModBlocks.FROST_ANVIL.get())) {
                 frostEnergy += soundWave.loudness();
             } else if (block.equals(ModBlocks.EMBER_ANVIL.get())) {
                 emberEnergy += soundWave.loudness();
             }
        }
        return frostEnergy == emberEnergy && frostEnergy != 0;
    }
    
    @Override
    public ResourceLocation getId() {
        return AnvilCraftReverberation.of("ablation");
    }
}
