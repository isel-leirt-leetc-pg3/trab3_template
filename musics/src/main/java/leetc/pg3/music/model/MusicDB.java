package leetc.pg3.music.model;

import java.util.*;

/**
 * TODO ...
 */
public class MusicDB {
    // Date

    // TODO - not is completed

    public MusicDB() {   }

    /**
     * Methods to add songs, songs, albums and playlists
     */
    public Song addSong(Song song) {
        return null;
    }
    public Album addAlbum(Album album) {
       return null;
    }

    public List<MusicItem> addMusics( List<MusicItem> items ) {
        return null;
    }

    public Playlist addRandomPlayList(String name, String[] genres,
                                      int totalSongs, long maxDuration) {
        return null;
    }

    /**
     * Methods to get the song, album or playlist with given title
     */
    public Song getSong(String t)        { return null; }
    public Album getAlbum(String t)      { return null; }
    public Playlist getPlaylist(String t){ return null; }

    /**
     * Methods to get all songs, albums, playlists, artists and genres.
     */
    public Iterable<Song> getSongs()         { return null;  }
    public Iterable<Album> getAlbums()       { return null;  }
    public Iterable<Playlist> getPlaylists() { return null;  }
    public Iterable<String> getArtists()     { return null;  }
    public Iterable<String> getGenres()      { return null;  }

    /**
     * Method to get the songs of the specified artist.
     */
    public Iterable<Song> getSongs(String artist) {
        return null;
    }

    /**
     * Method for getting songs that have at least one of the specified genres.
     */
    public Iterable<Song> getSongs(String ... genres) { return null;  }

}
