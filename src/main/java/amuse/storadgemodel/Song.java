package amuse.storadgemodel;

import java.net.MalformedURLException;
import java.net.URL;

public final class Song {

    private String artist;
    private String title;
    private long duration;

    private URL url;
    private long lyricsId;
    private long genreId;

    public Song(String title, String artist, long duration, String url) throws MalformedURLException {
        setTitle(title);
        setArtist(artist);
        setDuration(duration);
        this.url = new URL(url);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public URL getUrl() {
        return url;
    }

    public long getLyricsId() {
        return lyricsId;
    }

    public long getGenreId() {
        return genreId;
     }
}