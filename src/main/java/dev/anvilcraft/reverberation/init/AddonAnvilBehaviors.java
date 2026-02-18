package dev.anvilcraft.reverberation.init;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.anvil.ClangCrystalBehavior;
import dev.anvilcraft.reverberation.anvil.SoundReactorBehavior;
import dev.dubhe.anvilcraft.api.event.AnvilBehaviorRegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = AnvilCraftReverberation.MOD_ID)
public class AddonAnvilBehaviors {
    @SubscribeEvent
    public static void register(AnvilBehaviorRegisterEvent event) {
        event.registerBehavior(AddonBlocks.CLANG_CRYSTAL.get(), new ClangCrystalBehavior());
        event.registerBehavior(AddonBlocks.ANVIL_SOUND_REACTOR.get(), new SoundReactorBehavior());
    }
}
