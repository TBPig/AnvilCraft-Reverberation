package dev.anvilcraft.reverberation.init;

import dev.anvilcraft.reverberation.api.Melody;
import dev.anvilcraft.reverberation.melody.EqualMelody;
import dev.anvilcraft.reverberation.melody.HigherMelody;
import dev.anvilcraft.reverberation.melody.LowerMelody;

@SuppressWarnings("unused")
public class AddonMelodies {
    public static final LowerMelody LOWER_MELODY = Melody.register(LowerMelody::new);
    public static final HigherMelody HIGHER_MELODY = Melody.register(HigherMelody::new);
    //    public static final AblationMelody ABLATION_MELODY = Melody.register(AblationMelody::new);
    public static final EqualMelody EQUAL_MELODY = Melody.register(EqualMelody::new);

    public static void register() {
    }
}
