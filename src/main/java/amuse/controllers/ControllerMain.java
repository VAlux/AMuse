package amuse.controllers;

import amuse.api.APIProvider;
import amuse.download.DownloadManager;
import amuse.download.DownloadTask;
import amuse.storadgemodel.SoundStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ControllerMain implements Initializable {

  private static final String TARGET_SAVE_FOLDER = "amuse_downloaded_songs";
  @FXML
  private TableView<DownloadTask> twSongsList;
  @FXML
  private Button btnDownloadSelected;
  @FXML
  private Button btnDownloadAll;
  @FXML
  private Label lblSongName;
  @FXML
  private Pagination pgnPagination;
  @FXML
  private ControllerPlayer playerController;

  private DownloadManager downloadManager;
  private SoundStorage soundStorage;
  private ObservableList<DownloadTask> downloadTasks;
  private static final int ROWS_PER_PAGE = 20;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    twSongsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    soundStorage = createSoundStorage();
    downloadManager = new DownloadManager();
    downloadTasks = createDownloadTasks(soundStorage);
    populateProgressColumns();
    addActionListeners();
    pgnPagination.setPageFactory(this::createPage);
    pgnPagination.setPageCount(downloadTasks.size() / ROWS_PER_PAGE);
  }

  private Path resolveSaveFolder() {
    final File targetSaveFolder = Paths.get(System.getProperty("user.dir"), TARGET_SAVE_FOLDER).toFile();
    final boolean exists = targetSaveFolder.exists();
    if (!exists) {
      final boolean folderCreated = targetSaveFolder.mkdir();
      if (folderCreated) {
        System.out.println("Save folder does not existed. Created new one at: " + targetSaveFolder.getAbsolutePath());
      }
    }
    return targetSaveFolder.toPath();
  }

  private Node createPage(final int pageIndex) {
    final int from = pageIndex * ROWS_PER_PAGE;
    final int to = Math.min(from + ROWS_PER_PAGE, downloadTasks.size());
    twSongsList.setItems(FXCollections.observableList(downloadTasks.subList(from, to)));
    return new BorderPane(twSongsList);
  }

  private ObservableList<DownloadTask> createDownloadTasks(SoundStorage storage) {
    final ObservableList<DownloadTask> downloadTasks = twSongsList.getItems();
    final Path saveFolder = resolveSaveFolder();
    final List<DownloadTask> tasks = storage.getSongs()
            .stream()
            .map(song -> new DownloadTask(song, saveFolder))
            .collect(Collectors.toList());
    downloadTasks.addAll(tasks);
    return downloadTasks;
  }

  private void populateProgressColumns() {
    TableColumn<DownloadTask, Double> progressColumn = new TableColumn<>("Progress");
    progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));
    progressColumn.setCellFactory(ProgressBarTableCell.<DownloadTask>forTableColumn());
    twSongsList.getColumns().add(progressColumn);
  }

  private SoundStorage createSoundStorage() {
    final SoundStorage storage = new SoundStorage();
    try {
      final HttpEntity response = APIProvider.getInstance().executeQuery();
      assert response != null;
      try (InputStream stream = response.getContent()) {
        storage.populate(IOUtils.toString(stream));
      } catch (InterruptedException | ExecutionException | ParseException e) {
        throw new IOException("Problem with input stream reading: " + e.getMessage());
      }
    } catch (IOException | URISyntaxException e) {
      System.err.println("problem occurred while populating sound storage. \n" + e.getMessage());
    }
    return storage;
  }

  private void addActionListeners() {
    btnDownloadSelected.setOnAction(actionEvent -> {
              for (Integer index : twSongsList.getSelectionModel().getSelectedIndices()) {
                downloadManager.performDownload(downloadTasks.get(index));
              }
            }
    );

    btnDownloadAll.setOnAction(actionEvent -> {
      final long delay = 100L;
      for (DownloadTask downloadTask : downloadTasks) {
        downloadManager.performDownload(downloadTask);
        try {
          Thread.sleep(delay);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    twSongsList.getSelectionModel().selectedItemProperty().addListener((observableValue, task, task2) -> {
      if (task2 != null) {
        playerController.chooseSong(task2.getSong());
        lblSongName.setText(task2.getArtist() + " :: " + task2.getName());
      }
    });
  }
}