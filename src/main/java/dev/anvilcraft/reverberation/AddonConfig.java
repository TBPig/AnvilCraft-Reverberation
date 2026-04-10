package dev.anvilcraft.reverberation;

import dev.anvilcraft.lib.config.BoundedDiscrete;
import dev.anvilcraft.lib.config.CollapsibleObject;
import dev.anvilcraft.lib.config.Comment;
import dev.anvilcraft.lib.config.Config;

@Config(name = AnvilCraftReverberation.MOD_ID)
public class AddonConfig {
    // 音能发电机
    @CollapsibleObject
    public SoundGenerator soundGenerator = new SoundGenerator();

    public static class SoundGenerator {
        @Comment("Maximum power output of Sound Generator")
        @BoundedDiscrete(min = 1, max = 128000000)
        public int maxPower = 64;

        @Comment("Power per energy unit")
        @BoundedDiscrete(min = 0, max = 1000000)
        public float powerPerEnergy = 1.0f;

        @Comment("Multiplier when Higher Melody is satisfied")
        @BoundedDiscrete(min = 1, max = 1000)
        public int higherMelodyMultiplier = 8;
    }
}
