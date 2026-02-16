package dev.anvilcraft.reverberation.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Melody {
    HIGHER,
    LOWER,
    ABLATION;

    public static Set<Melody> judge(MergeSoundStore mergeSoundStore) {
        Set<Melody> melodies = new HashSet<>();
        List<MergeSound> soundHistory = mergeSoundStore.getSoundHistory();
        int num = soundHistory.size();

        if (num >= 1) {
            MergeSound sound = soundHistory.get(num - 1);
            List<SoundWave> soundWaves = sound.getSoundWaves();
            int frostEnergy = 0;
            int emberEnergy = 0;
            for (SoundWave soundWave : soundWaves) {
                if (soundWave.kind() == SoundKind.FROST) {
                    frostEnergy += soundWave.energy();
                } else if (soundWave.kind() == SoundKind.EMBER) {
                    emberEnergy += soundWave.energy();
                }
            }
            if (frostEnergy == emberEnergy) melodies.add(ABLATION);
        }

        if (num >= 2) {
            MergeSound sound1 = soundHistory.get(num - 1);
            MergeSound sound2 = soundHistory.get(num - 2);
            if (sound1.getEnergy() > sound2.getEnergy()) {
                melodies.add(HIGHER);
            } else if (sound1.getEnergy() < sound2.getEnergy()) {
                melodies.add(LOWER);
            }
        }

        return melodies;
    }
}
