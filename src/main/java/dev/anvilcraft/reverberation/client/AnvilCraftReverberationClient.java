package dev.anvilcraft.reverberation.client;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = AnvilCraftReverberation.MOD_ID, dist = Dist.CLIENT)
public class AnvilCraftReverberationClient {
    public AnvilCraftReverberationClient(IEventBus modBus, ModContainer container) {
    }
}
