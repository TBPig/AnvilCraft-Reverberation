package dev.anvilcraft.reverberation.api;

import dev.anvilcraft.reverberation.recipe.SoundReactorRecipe;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 合并的音波；附带了存储功能。
 * 因此可以表现旋律是否启用
 */
public class MergeSoundStore extends MergeSound {
    public static final int DEFAULT_CAPACITY = 8;

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

    public boolean isValid(SoundReactorRecipe recipe) {
        if (soundHistory.isEmpty()) return false;

        MergeSound lastSound = soundHistory.getLast();
        int currentEnergy = lastSound.getEnergy();
        int currentSourceNum = lastSound.getSourceNum();
        Set<Timbre> currentTimbres = lastSound.getTimbreSet();

        // 检查能量范围
        if (recipe.getMinEnergy() != null && currentEnergy < recipe.getMinEnergy()) {
            return false;
        }
        if (recipe.getMaxEnergy() != null && currentEnergy > recipe.getMaxEnergy()) {
            return false;
        }

        // 检查声源数量范围
        if (recipe.getMinSourceNum() != null && currentSourceNum < recipe.getMinSourceNum()) {
            return false;
        }
        if (recipe.getMaxSourceNum() != null && currentSourceNum > recipe.getMaxSourceNum()) {
            return false;
        }

        // 检查必需音色是否存在
        for (Timbre requiredTimbre : recipe.getRequiredTimbres()) {
            boolean found = false;
            for (Timbre currentTimbre : currentTimbres) {
                if (currentTimbre.satisfy(requiredTimbre)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }
}
