package amuse.download;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager {

    private Path saveFolder;
    private ExecutorService downloadTasksExecutor;

    public DownloadManager() {
        this.saveFolder = Paths.get(System.getProperty("user.dir"));
        downloadTasksExecutor = Executors.newCachedThreadPool();
    }

   public void downloadSong(final DownloadTask task){
       downloadTasksExecutor.execute(task);
    }

    public void setSaveFolder(final String saveFolder) {
        this.saveFolder = Paths.get(saveFolder);
    }

    public void cancelAll() {
        downloadTasksExecutor.shutdownNow();
    }
}
