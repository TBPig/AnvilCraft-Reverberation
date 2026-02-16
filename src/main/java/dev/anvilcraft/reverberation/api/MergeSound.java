package dev.anvilcraft.reverberation.api;

import lombok.Getter;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@Getter
public class MergeSound {
    private Set<SoundKind> kinds;
    @Getter
    private int energy;
    private Set<BlockPos> positions;

    public MergeSound() {
        this.kinds = Set.of();
        this.energy = 0;
        this.positions = Set.of();
    }

    public MergeSound add(SoundWave soundWave) {
        this.kinds.add(soundWave.kind());
        this.energy += soundWave.energy();
        this.positions.add(soundWave.pos());

        return this;
    }

    public MergeSound add(List<SoundWave> soundWaves) {
        for (SoundWave soundWave : soundWaves) {
            this.add(soundWave);
        }
        return this;
    }

    public int getSourceNum() {
        return positions.size();
    }
}
