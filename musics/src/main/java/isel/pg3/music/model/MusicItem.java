package isel.pg3.music.model;

import isel.pg3.music.utils.Mp3Player;

import java.util.Collection;
import java.util.*;

public abstract class MusicItem  implements Comparable<MusicItem>, Iterable<Song> {
    private final String title; // the music item title
    private final Set<String> artists;
    protected final Set<String> genres;

    protected static boolean isValidName(String name) {
        return name != null && !name.isEmpty();
    }

    private void append(StringBuilder sb, Collection<String> col ) {
        Iterator<String> it= col.iterator();
        if ( it.hasNext() ) {
            sb.append(", ");
            sb.append(it.next());
        }
    }

    private static String getFirst(Collection<String> col, String name) {
        return col.isEmpty() ?  "unknown " + name : col.iterator().next();
    }

    // constructors

    protected MusicItem(String title) {
        this(title, new TreeSet<>(), new TreeSet<>());
    }

    protected MusicItem(String title, String artist, String genre) {
        this(title);
        if (isValidName(artist)) artists.add(artist);
        if (isValidName(genre)) genres.add( genre );
    }

    protected MusicItem(String title, Set<String> artists, Set<String> genres) {
        this.title = title;
        this.artists = artists ;
        this.genres = genres;
    }


    /**
     * to get a string representation of music item
     */
    @Override
    public String toString() {
        StringBuilder sb= new StringBuilder( '\''+ title +'\'');
        if (getDuration() != 0) {
            sb.append(" (").append( getTime()).append(")");
        }
        append( sb, artists );
        append( sb, genres );
        return sb.toString();
    }

    /**
     * Compare this MusicItem with another
     * @param other
     * @return a negative value if "this" is lesser than "other"
     *         a positive value if "this" is greater than "other"
     *         0 if they are equal
     */
    @Override
    public int compareTo(MusicItem other) {
        // just compare correspondent titles
        return title.compareTo(other.getTitle());
    }


    // setters, getters

    public Collection<String> getGenres()  { return Collections.unmodifiableCollection(genres);  }

    public Collection<String> getArtists() { return Collections.unmodifiableCollection(artists); }

    protected void addArtists(Collection<String> artists)  {
        this.artists.addAll(artists);
    }

    protected void addGenres(Collection<String> genres)   { this.genres.addAll( genres); }

    public String getTitle()  { return title; }

    public String getArtist() { return getFirst( artists, " artist"); }

    public String getGenre()  { return getFirst( genres, " genre"); }

    public String getTime() {
        long t = getDuration();
        return String.format("%02d:%02d", t / 60, t % 60);
    }

    // abstract methods


    public abstract long getDuration();

    public abstract String getPath();

    public abstract void playOn(Mp3Player player );


}
