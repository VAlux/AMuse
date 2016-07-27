package amuse;

import amuse.controllers.ControllerAuth;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layout/auth.fxml"));
    Parent root = loader.load();
    ControllerAuth authController = loader.getController();
    authController.setParentStage(primaryStage);
    primaryStage.setTitle("Authorization");
    primaryStage.setScene(new Scene(root, 700, 400));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
