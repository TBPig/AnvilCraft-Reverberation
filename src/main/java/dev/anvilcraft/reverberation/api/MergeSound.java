package dev.anvilcraft.reverberation.api;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

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
    private int energy = 0;
    private final Set<Pitch> pitchSet = new HashSet<>();
    private final Set<Block> timbreSet = new HashSet<>();
    private final Set<BlockPos> positions = new HashSet<>();
    private final List<SoundWave> soundWaves = new ArrayList<>();

    public MergeSound() {
    }

    public MergeSound add(SoundWave soundWave) {
        this.energy += soundWave.loudness();
        this.pitchSet.add(soundWave.pitch());
        this.timbreSet.add(soundWave.timbre());
        this.positions.add(soundWave.pos());
        this.soundWaves.add(soundWave);
        return this;
    }

    public MergeSound copy() {
        MergeSound mergeSound = new MergeSound();
        mergeSound.energy = this.energy;
        mergeSound.pitchSet.addAll(this.pitchSet);
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
        this.pitchSet.clear();
        this.timbreSet.clear();
        this.positions.clear();
        this.soundWaves.clear();
    }
}
