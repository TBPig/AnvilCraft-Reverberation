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
        provider.add("tooltip.jade.anvilcraft_reverberation.merge_sound_pillar.energy", "Sound Energy: %s");
        provider.add("tooltip.jade.anvilcraft_reverberation.merge_sound_pillar.source_num", "Source Num: %s");

        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.progress", "Sound Sequence Etching Progress");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.step", "§8Step: %d / %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.next", "§bNext: %s");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.energy", "Energy [%d-%d]");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.min_energy", "Min Energy %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.max_energy", "Max Energy %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.sources", "Sources [%d-%d]");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.min_sources", "Min Sources %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.max_sources", "Max Sources %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.timbre", "Timbre: %s");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.melody", "Melody: %s");
    }
}
