package dev.anvilcraft.reverberation.api;

import lombok.Getter;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 合并的音波；
 * 纪录声源数量，砧音能，砧音种类
 */
@Getter
public class MergeSound {
    private final Set<SoundKind> kinds = new HashSet<>();
    private int energy = 0;
    private final Set<BlockPos> positions = new HashSet<>();
    private final List<SoundWave> soundWaves = new ArrayList<>();

    public MergeSound() {
    }

    public MergeSound add(SoundWave soundWave) {
        this.kinds.add(soundWave.kind());
        this.energy += soundWave.energy();
        this.positions.add(soundWave.pos());
        this.soundWaves.add(soundWave);
        return this;
    }

    public static MergeSound of(List<SoundWave> soundWaves) {
        MergeSound mergeSound = new MergeSound();
        for (SoundWave soundWave : soundWaves) {
            mergeSound.add(soundWave);
        }
        return mergeSound;
    }

    public MergeSound copy() {
        MergeSound mergeSound = new MergeSound();
        mergeSound.kinds.addAll(this.kinds);
        mergeSound.energy = this.energy;
        mergeSound.positions.addAll(this.positions);
        mergeSound.soundWaves.addAll(this.soundWaves);
        return mergeSound;
    }

    public int getSourceNum() {
        return positions.size();
    }

    public void clear() {
        this.kinds.clear();
        this.energy = 0;
        this.positions.clear();
        this.soundWaves.clear();
    }
}
