package amuse.storadgemodel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class SoundStorage {
    ArrayList<Song> songs;

    public SoundStorage() {
        songs = new ArrayList<>();
    }

    public void populate(String source){
        JSONParser parser = new JSONParser();
        try {
            JSONObject parsedSource = (JSONObject) parser.parse(source);
            JSONArray rawList = (JSONArray) parsedSource.get("response");
            JSONObject listEntry;

            if(rawList == null){
                listEntry = (JSONObject)parsedSource.get("error");
                System.err.println(listEntry.get("error_msg"));
                return;
            }
            for (Object elem : rawList) {
                listEntry = (JSONObject) elem;

                final String title = (String) listEntry.get("title");
                final String artist = (String) listEntry.get("artist");
                final String url = (String) listEntry.get("url");
                final long duration = (long) listEntry.get("duration");

                songs.add(new Song(title, artist, duration, url));
            }
        } catch (ParseException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public Song getSong(int index) {
        return this.songs.get(index);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
}