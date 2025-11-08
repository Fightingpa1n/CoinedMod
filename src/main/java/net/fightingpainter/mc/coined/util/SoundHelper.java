package net.fightingpainter.mc.coined.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;

/** Helper class for playing sounds and stuff */
public class SoundHelper { //TODO: add comments to all methods

    //============================== Sound Manager ==============================\\
    public static SoundManager getSoundManager() {
        return Minecraft.getInstance().getSoundManager();
    }

    //============================== Sound Playing ==============================\\
    //=========== Start Sound Instance ===========\\
    public static SoundInstance playSound(SoundEvent sound, float volume, float pitch) {
        SoundInstance instance = SimpleSoundInstance.forUI(sound, volume, pitch);
        getSoundManager().play(instance);
        return instance;
    }

    //=========== Stop Sound Instance ===========\\
    public static void stopSound(SoundInstance sound) {
        getSoundManager().stop(sound);
    }

}
