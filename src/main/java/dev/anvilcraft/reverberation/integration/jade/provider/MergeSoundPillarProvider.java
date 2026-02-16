package dev.anvilcraft.reverberation.integration.jade.provider;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.block.entity.MergeSoundPillarBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum MergeSoundPillarProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag serverData = accessor.getServerData();
        if (serverData.contains("energy")) {
            tooltip.add(Component.translatable(
                "tooltip.jade.anvilcraft_reverberation.merge_sound_pillar.energy",
                serverData.getInt("energy")
            ));
        }

    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof MergeSoundPillarBlockEntity entity) {
            tag.putInt("energy", entity.getMergeSound().getEnergy());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return AnvilCraftReverberation.of("merge_sound_pillar");
    }
}
