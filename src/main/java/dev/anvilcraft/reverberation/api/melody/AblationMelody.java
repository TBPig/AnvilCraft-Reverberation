package dev.anvilcraft.reverberation.api.melody;

import dev.anvilcraft.reverberation.api.MergeSound;
import dev.anvilcraft.reverberation.api.MergeSoundStore;
import dev.anvilcraft.reverberation.api.Pitch;
import dev.anvilcraft.reverberation.api.SoundWave;

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
            if (soundWave.pitch() == Pitch.FROST) {
                frostEnergy += soundWave.loudness();
            } else if (soundWave.pitch() == Pitch.EMBER) {
                emberEnergy += soundWave.loudness();
            }
        }
        return frostEnergy == emberEnergy;
    }
}
