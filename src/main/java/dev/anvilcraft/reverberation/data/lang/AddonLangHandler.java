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
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.loops", "Loops: %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.all_steps", "All Steps: %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_sequence.next", "§bNext: %s");
        provider.add("tooltip.anvilcraft_reverberation.sound_require.energy", "Energy [%d-%d]");
        provider.add("tooltip.anvilcraft_reverberation.sound_require.min_energy", "Min Energy %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_require.max_energy", "Max Energy %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_require.sources", "Sources [%d-%d]");
        provider.add("tooltip.anvilcraft_reverberation.sound_require.min_sources", "Min Sources %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_require.max_sources", "Max Sources %d");
        provider.add("tooltip.anvilcraft_reverberation.sound_require.timbre", "Timbre: %s");
        provider.add("tooltip.anvilcraft_reverberation.sound_require.melody", "Melody: %s");

        provider.add("gui.anvilcraft_reverberation.category.sound_reactor", "Sound Reactor");
        provider.add("gui.anvilcraft_reverberation.category.sound_sequence_etching", "Sound Sequence Etching");

        provider.add("block.anvilcraft_reverberation.sound_generator.placement_requires_pillar", "Sound Generator must be placed above a Merge Sound Pillar");

        provider.add("config.jade.plugin_anvilcraft_reverberation.merge_sound_pillar", "Merge Sound Pillar");
    }
}
