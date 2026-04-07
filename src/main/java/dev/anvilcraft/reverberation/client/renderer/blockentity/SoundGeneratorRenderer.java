package dev.anvilcraft.reverberation.client.renderer.blockentity;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import dev.anvilcraft.reverberation.block.entity.SoundGeneratorBlockEntity;
import dev.dubhe.anvilcraft.client.renderer.blockentity.PowerProducerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class SoundGeneratorRenderer extends PowerProducerRenderer<SoundGeneratorBlockEntity> {
    public static final ModelResourceLocation MODEL =
        ModelResourceLocation.standalone(AnvilCraftReverberation.of("block/sound_generator_head"));

    public SoundGeneratorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected ModelResourceLocation getModel() {
        return MODEL;
    }
}
