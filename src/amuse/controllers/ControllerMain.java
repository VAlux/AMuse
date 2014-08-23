package amuse.controllers;

import amuse.api.APIProvider;
import amuse.download.DownloadManager;
import amuse.download.DownloadTask;
import amuse.storadgemodel.Song;
import amuse.storadgemodel.SoundStorage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private SoundStorage storage;
    private DownloadManager downloadManager;
    private ObservableList<DownloadTask> downloadTasks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        storage = new SoundStorage();
        downloadManager = new DownloadManager();
        downloadTasks = twSongsList.getItems();

        try {
            HttpEntity response = APIProvider.getInstance().executeQuery();
            try (InputStream stream = response.getContent()){
                storage.populate(IOUtils.toString(stream));
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("problem occurred while populating sound storage. \n" + e.getMessage());
            return;
        }

        for (Song song : storage.getSongs()) {
            downloadTasks.add(new DownloadTask(song));
        }

        TableColumn<DownloadTask, Double> progressColumn = new TableColumn<>("Progress");
        progressColumn.setCellValueFactory(new PropertyValueFactory<DownloadTask, Double>("progress"));
        progressColumn.setCellFactory(ProgressBarTableCell.<DownloadTask> forTableColumn());
        twSongsList.getColumns().add(progressColumn);

        addActionListeners();
    }

    private void addActionListeners(){
        btnDownloadSelected.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                downloadManager.downloadSong(downloadTasks.get(twSongsList.getSelectionModel().getSelectedIndex()));
            }
        });

        btnDownloadAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                final long delay = 100L;
                for (DownloadTask downloadTask : downloadTasks) {
                    downloadManager.downloadSong(downloadTask);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        twSongsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DownloadTask>() {
            @Override
            public void changed(ObservableValue<? extends DownloadTask> observableValue, DownloadTask task, DownloadTask task2) {
                playerController.setSong(task2.getSong());
                lblSongName.setText(task2.getArtist() + " :: " + task2.getName());
            }
        });
    }
}