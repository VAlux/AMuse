package amuse.controllers;

import amuse.api.APIProvider;
import amuse.download.DownloadManager;
import amuse.download.DownloadTask;
import amuse.storadgemodel.SoundStorage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ControllerMain implements Initializable{

    @FXML
    private TableView<DownloadTask> twSongsList;
    @FXML
    private Button btnDownloadSelected;
    @FXML
    private Button btnDownloadAll;
    @FXML
    private ControllerPlayer playerController;
    @FXML
    private Label lblSongName;

    private DownloadManager downloadManager;
    private ObservableList<DownloadTask> downloadTasks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final SoundStorage storage = new SoundStorage();
        downloadManager = new DownloadManager();
        downloadTasks = twSongsList.getItems();

        try {
            HttpEntity response = APIProvider.getInstance().executeQuery();
            assert response != null;
            try (InputStream stream = response.getContent()){
                storage.populate(IOUtils.toString(stream));
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("problem occurred while populating sound storage. \n" + e.getMessage());
            return;
        }

        downloadTasks.addAll(storage.getSongs().stream().map(DownloadTask::new).collect(Collectors.toList()));

        TableColumn<DownloadTask, Double> progressColumn = new TableColumn<>("Progress");
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));
        progressColumn.setCellFactory(ProgressBarTableCell.<DownloadTask> forTableColumn());
        twSongsList.getColumns().add(progressColumn);

        addActionListeners();
    }

    private void addActionListeners(){
        btnDownloadSelected.setOnAction(actionEvent ->
                downloadManager.downloadSong(downloadTasks.get(twSongsList.getSelectionModel().getSelectedIndex())));

        btnDownloadAll.setOnAction(actionEvent -> {
            final long delay = 100L;
            for (DownloadTask downloadTask : downloadTasks) {
                downloadManager.downloadSong(downloadTask);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        twSongsList.getSelectionModel().selectedItemProperty().addListener((observableValue, task, task2) -> {
            playerController.setSong(task2.getSong());
            lblSongName.setText(task2.getArtist() + " :: " + task2.getName());
        });
    }
}