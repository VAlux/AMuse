package amuse.download;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager {

  private static final String DEFAULT_SAVE_FOLDER = System.getProperty("user.dir");

  private Path saveFolder;
  private ExecutorService downloadTasksExecutor;

  public DownloadManager() {
    this.saveFolder = Paths.get(DEFAULT_SAVE_FOLDER);
    downloadTasksExecutor = Executors.newCachedThreadPool();
  }

  public void performDownload(final DownloadTask task) {
    downloadTasksExecutor.execute(task);
  }

  public void setSaveFolder(final String saveFolder) {
    this.saveFolder = Paths.get(saveFolder);
  }

  public void cancelAll() {
    downloadTasksExecutor.shutdownNow();
  }
}
