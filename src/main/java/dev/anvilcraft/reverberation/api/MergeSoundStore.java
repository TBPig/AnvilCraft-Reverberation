package dev.anvilcraft.reverberation.api;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 合并的音波；附带了存储功能。
 * 因此可以表现旋律是否启用
 */
public class MergeSoundStore extends MergeSound {
    public static final int DEFAULT_CAPACITY = 10;

    @Getter
    private final List<MergeSound> soundHistory = new ArrayList<>(DEFAULT_CAPACITY);

    public void store() {
        soundHistory.add(super.copy());
        if (soundHistory.size() > DEFAULT_CAPACITY) {
            soundHistory.removeFirst();
        }
        this.clear();
    }

    public MergeSound getLastSound() {
        return soundHistory.getLast() == null ? new MergeSound() : soundHistory.getLast();
    }
}
