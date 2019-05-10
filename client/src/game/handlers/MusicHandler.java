package game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import game.utils.Resources;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MusicHandler {

    private static Map<Integer, Music> musicMap = new ConcurrentHashMap<>();

    private static String musicPath = Resources.GAME_MUSIC_PATH;

    public static void load(){
        FileHandle file = Gdx.app.getFiles().internal(musicPath);

        if (!file.isDirectory())
            return;

        for (FileHandle tmp : file.list()) {
            if (tmp.extension().equals(Resources.GAME_MUSIC_EXTENSION)){
                Gdx.app.debug(SoundsHandler.class.getSimpleName(), "Cargando " + tmp.name());
                loadMusic(tmp);
            }else {
                String tmpExt = tmp.extension();
                Gdx.app.debug(SoundsHandler.class.getSimpleName(), tmpExt);
            }
        }

    }

    private static void loadMusic(FileHandle file) {
        Integer musicID;

        try {
            musicID = Integer.valueOf(file.nameWithoutExtension());
        } catch (NumberFormatException e) {
            Gdx.app.debug(SoundsHandler.class.getSimpleName(), "Error:" + file.name() + " should have a numeric name.", e);
            return;
        }

        Music music = Gdx.audio.newMusic(file);

        if (!musicMap.containsKey(musicID))
        {
            musicMap.put(musicID,music);
        }
    }

    public static void playMusic(int musicID){

        if (!musicMap.containsKey(musicID)) {
            Gdx.app.debug(SoundsHandler.class.getSimpleName(), "Error: tried to play sound index: " + musicID + ", but it was not loaded.");
            return;
        }
        //TODO: it should be played with a global configurable volume
        musicMap.get(musicID).play();
        musicMap.get(musicID).setLooping(true);
    }

    public static void stopMusic(int musicID){

        if (!musicMap.containsKey(musicID)) {
            Gdx.app.debug(SoundsHandler.class.getSimpleName(), "Error: tried to play sound index: " + musicID + ", but it was not loaded.");
            return;
        }
        //TODO: it should be played with a global configurable volume
        musicMap.get(musicID).stop();
    }

    public static void unload() {
        musicMap.forEach((k, v) -> v.dispose());
        musicMap.clear();
    }

    //TODO: WIP!!!!!!!!!!!!!!
    public static void PlayMIDI() {


        Sequencer sequencer = null;
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();

            //TODO: here the function will get the loaded file, this file will be loaded in load() using libgdx
            //InputStream file = new BufferedInputStream(new FileInputStream(new File("midifile.mid")));

            //sequencer.setSequence(file);

            // Starts playback of the MIDI data in the currently loaded sequence.
            sequencer.start();

        } catch (MidiUnavailableException e) {
            Gdx.app.debug(SoundsHandler.class.getSimpleName(), "Error on PlayMIDI(): Midi is not available.", e);
            return;
        }

    }
}
