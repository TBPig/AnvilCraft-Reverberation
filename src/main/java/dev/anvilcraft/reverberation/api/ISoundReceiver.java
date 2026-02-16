package dev.anvilcraft.reverberation.api;

public interface ISoundReceiver {

    default boolean receiveSound(SoundWave soundWave) {return false;}
}
