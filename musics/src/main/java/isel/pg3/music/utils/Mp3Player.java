package isel.pg3.music.utils;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class Mp3Player {
    private volatile long totalLength, pauseLength;
    private volatile InputStream mp3Stream;
    private volatile Player player;
    private volatile String filePath;
    private Iterator<String> iterator;
    private Thread playThread;

    // events
    public interface CompletedListListener {
        void onComplete();
    }
    public interface StartMusicListener {
        void onStartMusic(String title);
    }
    public interface ErrorListener {
        void onError(Exception error);
    }

    private static StartMusicListener DEFAULT_MUSIC_LISTENER = t -> {};
    private StartMusicListener startListener = DEFAULT_MUSIC_LISTENER;
    private static CompletedListListener DEFAULT_COMPLETED_LISTENER = () -> {};
    private CompletedListListener completedListener =DEFAULT_COMPLETED_LISTENER;
    private static ErrorListener DEFAULT_ERROR_LISTENER = (ex)-> {};
    private ErrorListener errorListener = DEFAULT_ERROR_LISTENER ;

    public void setStartMusicListener(StartMusicListener l) {
        startListener = ( l != null ) ? l : DEFAULT_MUSIC_LISTENER;
    }
    public void setCompletedListListener(CompletedListListener l) {
        completedListener = ( l != null ) ? l :DEFAULT_COMPLETED_LISTENER;
    }
    public void setErrorListener(ErrorListener l) {
        errorListener = ( l != null ) ? l :DEFAULT_ERROR_LISTENER;
    }

    private void fireCompletedEvent() {
        if (completedListener != null) {
            SwingUtilities.invokeLater(
                () -> completedListener.onComplete()
            );
        }
    }

    private void fireStartMusicEvent(String title) {
        if (startListener != null) {
            SwingUtilities.invokeLater(
                () -> startListener.onStartMusic(title)
            );
        }
    }

    private void fireErrorEvent(Exception  e) {
        if (errorListener != null) {
            SwingUtilities.invokeLater(
                () -> errorListener.onError(e)
            );
        }
    }

    public String getFilename( String filePath ) {
        int indexFirst = filePath.lastIndexOf('\\');
        if (indexFirst == -1) indexFirst= filePath.lastIndexOf('/');
        int indexLast = filePath.lastIndexOf('.');
        if (indexLast == -1) indexLast = filePath.length();
        return filePath.substring(indexFirst+1, indexLast);
    }

    private class PlayAbortedException  extends RuntimeException {

    }
    private void execPlay()
        throws IOException, JavaLayerException {

        try {
            //code for play button
            player = new Player(mp3Stream);

            player.play();//starting music

        }
        catch(Exception e) {
            fireErrorEvent(e);
        }
        finally {
            mp3Stream.close();
            if (player != null) {
                player.close();
                player = null;
            }
            else throw new PlayAbortedException();
        }
    }

    public void play(String filePath) {
       play(List.of(filePath));
    }

    public void play( Iterable<String> paths) {
        iterator = paths.iterator();
        if (!iterator.hasNext()) return;
        filePath = iterator.next();
        totalLength = 0;
        pauseLength = 0;
        resume();
    }

    public void resume() {
        playThread = new Thread(() -> {
            try {
                do {
                    mp3Stream =
                        new BufferedInputStream(new FileInputStream(filePath));
                    String title = null;
                    Mp3File mp3File = new Mp3File(filePath);
                    if (mp3File.hasId3v1Tag()) {
                        ID3v1 info = mp3File.getId3v1Tag();
                        title = info.getTitle();
                    }
                    if (title == null || title.isEmpty())
                        title = getFilename( filePath );
                    if (totalLength == 0) {
                        totalLength = mp3Stream.available();
                        fireStartMusicEvent(title);
                    }
                    if (pauseLength != 0)
                        mp3Stream.skip(totalLength - pauseLength);
                    execPlay();
                    pauseLength = 0;
                    totalLength = 0;
                    filePath  = iterator.hasNext() ? iterator.next() : null;
                }
                while(filePath != null);
                fireCompletedEvent();
            }
            catch(PlayAbortedException e) {

            }
            catch(Exception e) {
                fireErrorEvent(e);
            }

        });
        playThread.start();
    }


    public void stop() {
        //code for stop button
        try {
            if (player != null) {
                Player temp = player;
                player = null;
                temp.close();
                playThread.join();
                pauseLength = 0;
                iterator = null;
                filePath = null;
                fireCompletedEvent();
            }
        }catch(InterruptedException e) {

        }
    }

    public void pause() {
        //code for pause button
        if (player != null) {
            try {
                pauseLength = mp3Stream.available();
                Player temp = player;
                player = null;
                temp.close();

            } catch (IOException e1) {
                fireErrorEvent(e1);
            }
        }
    }


}
