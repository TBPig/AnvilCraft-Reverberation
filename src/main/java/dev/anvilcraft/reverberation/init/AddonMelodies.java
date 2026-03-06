package dev.anvilcraft.reverberation.init;

import dev.anvilcraft.reverberation.melody.AblationMelody;
import dev.anvilcraft.reverberation.melody.EqualMelody;
import dev.anvilcraft.reverberation.melody.HigherMelody;
import dev.anvilcraft.reverberation.melody.LowerMelody;
import dev.anvilcraft.reverberation.api.Melody;

@SuppressWarnings("unused")
public class AddonMelodies {
    public static final Melody LOWER_MELODY = Melody.register(LowerMelody::new);
    public static final Melody HIGHER_MELODY = Melody.register(HigherMelody::new);
    public static final Melody ABLATION_MELODY = Melody.register(AblationMelody::new);
    public static final Melody EQUAL_MELODY = Melody.register(EqualMelody::new);

    public static void register() {
    }
}
