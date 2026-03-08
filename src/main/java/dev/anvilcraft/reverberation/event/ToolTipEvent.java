package dev.anvilcraft.reverberation.event;

import dev.anvilcraft.reverberation.recipe.SoundSequenceEtchingRecipe;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ToolTipEvent {

    @SubscribeEvent
    public static void addToItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() == null) return;

        SoundSequenceEtchingRecipe.addToTooltip(event);
    }
}
