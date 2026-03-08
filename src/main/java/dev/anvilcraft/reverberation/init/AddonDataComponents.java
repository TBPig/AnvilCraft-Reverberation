package dev.anvilcraft.reverberation.init;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.component.SoundSequenceData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 数据组件注册
 */
@SuppressWarnings("unused")
public class AddonDataComponents {
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = 
        DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, AnvilCraftReverberation.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SoundSequenceData>> SOUND_SEQUENCE =
        DATA_COMPONENT_TYPES.register("sound_sequence", () ->
            DataComponentType.<SoundSequenceData>builder()
                .persistent(SoundSequenceData.CODEC)
                .networkSynchronized(SoundSequenceData.STREAM_CODEC)
                .build()
        );

    public static void register(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }
}
