package isel.pg3.music.model;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import isel.pg3.exceptions.FindMusicException;
import isel.pg3.exceptions.MusicDbException;

import java.io.File;
import java.util.*;

public class MusicItemFinder {
    // all official mp3 genres
    private static String[] genres = {
        "Blues", "Classic Rock","Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop",
        "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap","Reggae",
        "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks",
        "Soundtrack","Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion",
        "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip",
        "Gospel", "Noise", "Alternative Rock", "Bass", "Soul", "Punk", "Space", "Meditative",
        "Instrumental Pop", "Instrumental Rock","Ethnic", "Gothic", "Darkwave",
        "Techno-Industrial", "Electronic","Pop-Folk", "Eurodance","Dream", "Southern Rock",
        "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap","Pop/Funk", "Jungle",
        "Native US", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer",
        "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical",
        "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing",
        "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde",
        "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock",
        "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson",
        "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove",
        "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
        "Rhytmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",  "Acapella",
        "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie",
        "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal",
        "Black Metal", "Crossover", "Contemporary C", "Christian Rock", "Merengue", "Salsa",
        "Thrash Metal", "Anime", "JPop", "SynthPop",
    };

    public static String getGenre(int genreId) {
        return (genreId > 0 && genreId < genres.length) ? genres[genreId] : "";
    }

    public static boolean isAlbum(File folder) {
       return folder.isDirectory()&& getSubFolders(folder).length == 0 && getMusicFiles(folder).length > 0;
    }

    public static boolean isMusicFile(File f) {
        return f.isFile() && f.getName().endsWith(".mp3");
    }

    private static File[] getSubFolders(File folder) {
        return folder.listFiles(File::isDirectory);
    }

    private static File[] getMusicFiles(File folder) {
        return folder.listFiles(MusicItemFinder::isMusicFile);
    }

    public static Album getAlbum(File dirname) {
        if (!isAlbum(dirname))
            throw new MusicDbException("Not an album folder!");
        return buildAlbum( dirname );
    }

    private static Album buildAlbum(File dirname) {
        File[] fileSongs = getMusicFiles( dirname );
        List<Song> songs = new ArrayList<>();
        Set<String> artists = new LinkedHashSet<>();
        Set<String> genres = new TreeSet<>();
        for (File f : fileSongs) {
            Song s= buildSong( f, dirname.getName() );
            songs.add( s );
            artists.addAll(s.getArtists());
            genres.addAll(s.getGenres());
        }
        return new Album(dirname.getName(), dirname.getAbsolutePath(), songs, artists, genres);
    }

    public static Song getSong(File filename) {
        if (!isMusicFile(filename)) {
             throw new MusicDbException(filename.getName() + " not is song file!");
        }
        return buildSong( filename, "" );
   }
   private static Song buildSong(File filename, String dirName) {
         try {
            Mp3File mp3file = new Mp3File(filename.getAbsolutePath());
            String title = "";
            String artist = "Unkown";
            String genre = "Unknown";
            String album= dirName;
            int duration = (int) mp3file.getLengthInSeconds();
            if ( mp3file.hasId3v1Tag() ) {
                ID3v1 tagV1 = mp3file.getId3v1Tag();
                title = tagV1.getTitle();
                genre = getGenre(tagV1.getGenre());
                artist = tagV1.getArtist();
                if (album.isEmpty()) album = tagV1.getAlbum();
            }

            if ( title.isEmpty() ) {
                title = filename.getName();
                title = title.substring(0, title.length()-4);
            }
            return new Song(title, filename.getAbsolutePath(), genre, artist, duration, album);
        } catch (Exception e) {
            throw new FindMusicException("Error add song", e);
        }
    }


    public static List<MusicItem> getMusics(File rootFolder) {
        List<MusicItem> items = new ArrayList<>();
        if ( rootFolder.isDirectory() ) fromFolder(rootFolder, items);
        return items;
    }

    private static void fromFolder(File folder, List<MusicItem> items) {
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                if (isAlbum(f))
                    items.add(buildAlbum(f));
                else
                    fromFolder(f, items);
            } else if (isMusicFile(f)) {
                items.add(buildSong(f, ""));
            }
        }
    }


}
