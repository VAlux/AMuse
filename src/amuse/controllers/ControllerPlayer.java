package amuse.controllers;

import amuse.storadgemodel.Song;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPlayer implements Initializable {

    @FXML private Button btnPause;
    @FXML private Button btnStop;
    @FXML private Button btnPlay;
    @FXML private Label lblTrackTotalTime;
    @FXML private Label lblCurrentTrackTime;
    @FXML private Slider sldSongProgressSlider;

    private MediaPlayer player;
    private Media media;
    private Song selectedSong;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addActionListeners();
    }

    public void setSong(Song song) {
        this.selectedSong = song;
    }

    private void setMedia(final Song song) {
        if (song == null)
            return;

        this.media = new Media(song.getUrl().toString());
        if (player != null) {
            player.stop();
            player.dispose();
        }
        player = new MediaPlayer(media);
    }

    private void addActionListeners() {
        btnPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (player == null ||
                    player.getStatus() != MediaPlayer.Status.PLAYING ||
                    player.getStatus() != MediaPlayer.Status.PAUSED) {
                        setMedia(selectedSong);
                }
                if (player != null) {
                    player.play();
                }
            }
        });

        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (player != null)
                    player.pause();
            }
        });

        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (player != null)
                    player.stop();
            }
        });
    }
}
