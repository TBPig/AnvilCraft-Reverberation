package dev.anvilcraft.reverberation.integration.jei;

import dev.anvilcraft.reverberation.AnvilCraftReverberation;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class AddonJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return AnvilCraftReverberation.of("jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
//        registration.addItemStackInfo(
//            AddonItems.SPIRITUAL_COMPONENT.asStack(),
//            Component.translatable("jei.anvilcraft.pigsplus.info.spiritual_component", PROBABILITY*100)
//        );
    }
}
