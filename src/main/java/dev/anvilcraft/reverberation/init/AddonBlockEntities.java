package dev.anvilcraft.reverberation.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.anvilcraft.reverberation.block.entity.ClangCrystalBlockEntity;
import dev.anvilcraft.reverberation.block.entity.MergeSoundPillarBlockEntity;

import static dev.anvilcraft.reverberation.AnvilCraftReverberation.REGISTRATE;

public class AddonBlockEntities {
    public static final BlockEntityEntry<ClangCrystalBlockEntity> CLANG_CRYSTAL =
        REGISTRATE.blockEntity("clang_crystal", ClangCrystalBlockEntity::new)
            .validBlock(AddonBlocks.CLANG_CRYSTAL)
            .register();

    public static final BlockEntityEntry<MergeSoundPillarBlockEntity> MERGE_SOUND_PILLAR =
        REGISTRATE.blockEntity("merge_sound_pillar", MergeSoundPillarBlockEntity::new)
            .validBlock(AddonBlocks.MERGE_SOUND_PILLAR)
            .register();

    public static void register() {
    }
}