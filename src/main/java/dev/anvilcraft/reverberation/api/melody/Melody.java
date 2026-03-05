package dev.anvilcraft.reverberation.api.melody;

import dev.anvilcraft.reverberation.api.MergeSoundStore;

public abstract class Melody {
    public abstract boolean satisfy(MergeSoundStore mergeSoundStore);
}
