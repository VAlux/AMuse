package amuse.download;

import amuse.storadgemodel.Song;
import amuse.utils.NetworkUtils;
import amuse.utils.TimeUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DownloadTask extends Task<Void> {

    private Song song;
    private int songSize;
    private Path saveFolder;
    private ReadableByteChannel byteChannel;
    private FileOutputStream fileOutputStream;

    private final SimpleStringProperty artist = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty duration = new SimpleStringProperty("");

    public DownloadTask(Song song) {
        this(song, Paths.get(System.getProperty("user.dir")));
    }

    public DownloadTask(final Song song, final Path saveFolder) {
        this.song = song;
        this.saveFolder = saveFolder;
        artist.set(song.getArtist());
        name.set(song.getName());
        duration.set(TimeUtils.formatTime(song.getDuration()));
        songSize = -1;
    }

    @Override
    protected Void call() throws Exception {
        System.out.println("download started!");
        try {
            songSize = NetworkUtils.getContentSize(song.getUrl());
            this.updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, 1);

            byteChannel = new RBCWrapper(Channels.newChannel(song.getUrl().openStream()), this);
            fileOutputStream = new FileOutputStream(saveFolder.toString() + "/" + song.getName() + ".mp3");
            fileOutputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            byteChannel.close();

            this.updateProgress(1, 1);
        } catch (IOException e) {
            System.err.println("download failed!\n" + e.getMessage());
            return null;
        }
        System.out.println("download completed!");
        return null;
    }

    public void setProgress(final int progress) {
        this.updateProgress(progress, songSize);
    }

///FXML binding needs:
    public String getArtist() {
        return artist.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDuration() {
        return duration.get();
    }
///

    public Song getSong() {
        return song;
    }
}