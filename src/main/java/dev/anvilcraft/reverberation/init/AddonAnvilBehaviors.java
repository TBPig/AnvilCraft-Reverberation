package dev.anvilcraft.reverberation.init;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.dubhe.anvilcraft.api.event.AnvilBehaviorRegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = AnvilCraftReverberation.MOD_ID)
public class AddonAnvilBehaviors {
    @SubscribeEvent
    public static void register(AnvilBehaviorRegisterEvent event) {
    }
}
