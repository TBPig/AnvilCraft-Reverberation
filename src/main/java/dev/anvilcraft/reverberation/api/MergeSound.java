package dev.anvilcraft.reverberation.api;

import lombok.Getter;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 合并的音波；
 * 纪录砧音能，出现的音色，声源数量
 */
@Getter
public class MergeSound {
    private int energy = 0;
    private final Set<Timbre> timbreSet = new HashSet<>();
    private final Set<BlockPos> positions = new HashSet<>();
    private final List<SoundWave> soundWaves = new ArrayList<>();

    public MergeSound() {
    }

    public void add(SoundWave soundWave) {
        this.energy += soundWave.loudness();
        this.timbreSet.add(soundWave.timbre());
        this.positions.add(soundWave.pos());
        this.soundWaves.add(soundWave);
    }

    public MergeSound copy() {
        MergeSound mergeSound = new MergeSound();
        mergeSound.energy = this.energy;
        mergeSound.timbreSet.addAll(this.timbreSet);
        mergeSound.positions.addAll(this.positions);
        mergeSound.soundWaves.addAll(this.soundWaves);
        return mergeSound;
    }

    public int getSourceNum() {
        return positions.size();
    }

    public void clear() {
        this.energy = 0;
        this.timbreSet.clear();
        this.positions.clear();
        this.soundWaves.clear();
    }
}
