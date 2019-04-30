package com.ld44.game.audio;

import com.badlogic.gdx.audio.Sound;
import com.ld44.game.assets.Assets;

public enum SoundEffectType {
    Chunky_Explosion("audio/chunky_explosion.mp3"), Click("audio/crash.ogg"),
    Crash("audio/crash.ogg"), Beep("audio/beep.wav")
    ;

    SoundEffectType(String path) {
        this.SOUND = Assets.getInstance().getSoundEffect(path);
    }

    public final Sound SOUND;

    public static void playSound(SoundEffectType sound) {
        sound.SOUND.play(0.4f);
    }

}