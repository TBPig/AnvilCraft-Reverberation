package dev.anvilcraft.reverberation.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.anvilcraft.lib.config.ConfigData;
import dev.anvilcraft.reverberation.AddonConfig;

public class AddonLangHandler {

    /**
     * 语言文件初始化
     *
     * @param provider 提供器
     */
    public static void init(RegistrateLangProvider provider) {
        ConfigData.readConfigClass(provider, AddonConfig.class);

//        provider.add("config.jade.plugin_anvilcraft_pigsplus.enchanted_generator", "Enchanted Generator");
        provider.add("tooltip.jade.anvilcraft_reverberation.merge_sound_pillar.loudness", "Sound Energy: %s");
        provider.add("tooltip.jade.anvilcraft_reverberation.merge_sound_pillar.source_num", "Source Num: %s");
    }
}
