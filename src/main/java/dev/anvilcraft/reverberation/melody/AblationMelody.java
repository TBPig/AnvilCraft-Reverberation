package dev.anvilcraft.reverberation.melody;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.api.Melody;
import dev.anvilcraft.reverberation.api.MergeSoundStore;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class AblationMelody extends Melody {
    @Override
    public boolean satisfy(MergeSoundStore mergeSoundStore) {
//        4. 消融：当前周期的合音中，带有余烬金属块/浮霜金属块音色的*砧音波*，提供的能量相等
        // TODO: 1.6以后加入
//        List<MergeSound> soundHistory = mergeSoundStore.getSoundHistory();
//        if (soundHistory.isEmpty()) return false;
//
//        MergeSound sound = soundHistory.getLast();
//        List<SoundWave> soundWaves = sound.getSoundWaves();
//        int frostEnergy = 0;
//        int emberEnergy = 0;
//        for (SoundWave soundWave : soundWaves) {
//            Block block = soundWave.timbre().block();
//             if (block.equals(ModBlocks.FROST_ANVIL.get())) {
//                 frostEnergy += soundWave.loudness();
//             } else if (block.equals(ModBlocks.EMBER_ANVIL.get())) {
//                 emberEnergy += soundWave.loudness();
//             }
//        }
//        return frostEnergy == emberEnergy && frostEnergy != 0;
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return AnvilCraftReverberation.of("ablation");
    }
}
