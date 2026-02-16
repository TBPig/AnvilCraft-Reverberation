package dev.anvilcraft.reverberation.integration.jade;

import dev.anvilcraft.reverberation.block.MergeSoundPillarBlock;
import dev.anvilcraft.reverberation.block.entity.MergeSoundPillarBlockEntity;
import dev.anvilcraft.reverberation.integration.jade.provider.MergeSoundPillarProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@SuppressWarnings("unused")
@WailaPlugin
public class AddonJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
//        registration.registerItemStorage(CrabTrapStorageProvider.INSTANCE, CrabTrapBlockEntity.class);
        registration.registerBlockDataProvider(MergeSoundPillarProvider.INSTANCE, MergeSoundPillarBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
//        registration.registerItemStorageClient(CrabTrapStorageProvider.INSTANCE);
        registration.registerBlockComponent(MergeSoundPillarProvider.INSTANCE, MergeSoundPillarBlock.class);
    }
}
