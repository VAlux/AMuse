package amuse.storadgemodel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SoundStorage {

  private ArrayList<Song> songs;

  public SoundStorage() {
    songs = new ArrayList<>();
  }

  public void populate(String source) throws ExecutionException, InterruptedException, ParseException, MalformedURLException {
    JSONArray rawList = parseSource(source);
    for (Object elem : rawList) {
      final JSONObject listEntry = (JSONObject) elem;

      final String title = (String) listEntry.get("title");
      final String artist = (String) listEntry.get("artist");
      final String url = (String) listEntry.get("url");
      final long duration = (long) listEntry.get("duration");

      songs.add(new Song(title, artist, duration, url));
    }
  }

  private JSONArray parseSource(String source) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONArray rawList;
    final JSONObject parsedSource = (JSONObject) parser.parse(source);
    rawList = (JSONArray) parsedSource.get("response");
    JSONObject listEntry;

    if (rawList == null) {
      listEntry = (JSONObject) parsedSource.get("error");
      System.err.println(listEntry.get("error_msg"));
      return new JSONArray(); // return empty array in case of error.
    }

    return rawList;
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