package amuse.controllers;

import amuse.storadgemodel.Song;
import amuse.utils.TimeUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPlayer implements Initializable {

  @FXML
  private Button btnPause;
  @FXML
  private Button btnStop;
  @FXML
  private Button btnPlay;
  @FXML
  private Label lblTrackTotalTime;
  @FXML
  private Label lblCurrentTrackTime;
  @FXML
  private Slider sldSongProgressSlider;

  private MediaPlayer player;
  private Media media;
  private Song selectedSong;
  private Duration duration;
  private boolean stopRequested;
  private boolean isEndOfMedia;


  @Override

  public void initialize(URL url, ResourceBundle resourceBundle) {
    addActionListeners();
    sldSongProgressSlider.setMin(0.0d);
    sldSongProgressSlider.setMax(100.0d);
    sldSongProgressSlider.setValue(0.0d);
    sldSongProgressSlider.setDisable(true);
  }

  public void setSong(Song song) {
    this.selectedSong = song;
    lblCurrentTrackTime.setText(TimeUtils.formatTime(0L));
    lblTrackTotalTime.setText(TimeUtils.formatTime(selectedSong.getDuration()));
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
    addPlayerStatusChangesListeners();
    sldSongProgressSlider.setDisable(false);
  }

  private void addActionListeners() {
    btnPlay.setOnAction(actionEvent -> {
      if (player == null ||
              player.getStatus() != MediaPlayer.Status.PLAYING ||
              player.getStatus() != MediaPlayer.Status.PAUSED) {
        setMedia(selectedSong);
      } else if (isEndOfMedia) {
        player.seek(player.getStartTime());
        isEndOfMedia = false;
      }
      if (player != null) {
        player.play();
        ///TODO: change play button graphics.
      }
    });

    btnPause.setOnAction(actionEvent -> {
      if (player != null)
        player.pause();
    });

    btnStop.setOnAction(actionEvent -> {
      if (player != null) {
        player.stop();
        sldSongProgressSlider.setDisable(true);
      }
    });

    sldSongProgressSlider.valueProperty().addListener(observable -> {
      if (sldSongProgressSlider.isValueChanging()) {
        if (duration != null) {
          player.seek(duration.multiply(sldSongProgressSlider.getValue() / 100.0d));
        }
        updateIndicators();
      }
    });
  }

  private void addPlayerStatusChangesListeners() {
    player.setOnReady(() -> {
      duration = player.getMedia().getDuration();
      updateIndicators();
    });

    player.setOnEndOfMedia(() -> isEndOfMedia = true);

    player.setOnPaused(() -> {
      ///TODO: change play button graphics.
    });

    player.setOnPlaying(() -> {
      if (stopRequested) {
        player.pause();
        stopRequested = false;
      } else {
        ///TODO: change play button graphics
      }
    });

    player.currentTimeProperty().addListener((observableValue, duration1, duration2) -> {
      updateIndicators();
    });

    ///TODO: add volume slider value property listener
  }

  private void updateIndicators() {
    Platform.runLater(() -> {
      Duration currentTime = player.getCurrentTime();
      lblCurrentTrackTime.setText(TimeUtils.formatTime((long) currentTime.toSeconds()));
      sldSongProgressSlider.setDisable(duration.isUnknown());
      if (!sldSongProgressSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !sldSongProgressSlider.isValueChanging()) {
        sldSongProgressSlider.setValue(currentTime.divide(duration).toMillis() * 100.0);
      }

      ///TODO: add volume slider value handling.
    });
  }
}