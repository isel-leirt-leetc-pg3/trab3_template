package isel.pg3.music;

import isel.pg3.music.model.Song;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SongTests {

    @Test
    public void simple_song_creation() {
        Song song = new Song("Yesterday", null, "Pop", "Beatles", 180, null);
        assertEquals("Pop", song.getGenre());
    }
}
