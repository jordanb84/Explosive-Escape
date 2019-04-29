package com.ld44.game.audio;

import com.badlogic.gdx.audio.Music;
import com.ld44.game.assets.Assets;

public enum MusicType {
    Background("audio/bg.mp3"), Boss("audio/esa.ogg")
    ;

    MusicType(String path) {
        this.MUSIC = Assets.getInstance().getMusic(path);
    }

    public final Music MUSIC;

    public static Music currentPlaying;

    public static void startMusic(MusicType music, boolean loop) {
        if(MusicType.currentPlaying != null) {
            MusicType.currentPlaying.stop();
        }

        MusicType.currentPlaying = music.MUSIC;
        music.MUSIC.setVolume(0.3f);
        MusicType.currentPlaying.play();
    }

    public static void loopMusic(MusicType music) {
        if(MusicType.currentPlaying != null) {
            MusicType.currentPlaying.stop();
        }

        MusicType.currentPlaying = music.MUSIC;
        music.MUSIC.setVolume(0.3f);
        MusicType.currentPlaying.play();
        MusicType.currentPlaying.setLooping(true);
    }

    public static void stopMusic() {
        MusicType.currentPlaying.stop();
    }

}