package amuse.controllers;

import amuse.api.APIProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ControllerAuth implements Initializable {

  private final String OAUTH_URL = "https://oauth.vk.com/authorize?client_id=4130904&scope=audio,offline&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.0&response_type=token";
  private Stage parentStage;
  @FXML
  private WebView webView;

  public void setParentStage(Stage parentStage) {
    this.parentStage = parentStage;
  }

  public void initialize(URL url, ResourceBundle resourceBundle) {
    webView.getEngine().load(OAUTH_URL);
    webView.getEngine().setOnStatusChanged((webEvent) -> {
      if (webEvent.getSource() instanceof WebEngine) {
        WebEngine engine = (WebEngine) webEvent.getSource();
        String location = engine.getLocation();
        if (location.contains("access_token")) {
          try {
            URL locURL = new URL(location);
            String[] params = locURL.getRef().split("&");
            Map<String, String> paramsMap = new HashMap<>();

            for (String param : params) {
              final String[] splittedParam = param.split("=");
              final String name = splittedParam[0];
              final String value = splittedParam[1];
              paramsMap.put(name, value);
            }

            if (parentStage != null) {
              // creates instance of api provider and prepare it for queries.
              APIProvider.getInstance().init(paramsMap.get("user_id"), paramsMap.get("access_token"));
              Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("layout/main.fxml"));
              Scene mainScene = new Scene(root, 600, 500);
              mainScene.getStylesheets().add("styles/theme2.css");
              parentStage.setScene(mainScene);
              parentStage.setTitle("AMuse");
              // reset the local reference to avoid another responses handling(may cause multiple parentStage reinitialization).
              parentStage = null;
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });
  }
}