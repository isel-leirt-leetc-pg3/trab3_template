package leetc.pg3.music.model;

import leetc.pg3.music.utils.Mp3Player;

import java.util.*;


/**
 * TODO ...
 */
public class Album  extends MusicItem {
   /** Build a Album with:
     * @param title   - album title
     * @param path    - absolute pathname
     * @param songs   - collection of songs
     * @param artists - set of artists
     * @param genres  - set of artists
     */
    public Album(String title, String path, Collection<Song> songs,
                 Set<String> artists, Set<String> genres)  {
        super(title);
        // TODO
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void playOn(Mp3Player player) {

    }

    @Override
    public Iterator<Song> iterator() {
        return null;
    }
}