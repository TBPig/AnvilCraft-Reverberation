package dev.anvilcraft.reverberation.api.melody;

import dev.anvilcraft.reverberation.api.MergeSound;
import dev.anvilcraft.reverberation.api.MergeSoundStore;

import java.util.List;

public class LowerMelody extends Melody  {
    @Override
    public boolean satisfy(MergeSoundStore mergeSoundStore) {
        List<MergeSound> soundHistory = mergeSoundStore.getSoundHistory();
        if (soundHistory.size() < 2) return false;

        MergeSound sound1 = soundHistory.getLast();
        MergeSound sound2 = soundHistory.get(soundHistory.size() - 2);
        return sound1.getEnergy() * 2 <= sound2.getEnergy();
    }
}
