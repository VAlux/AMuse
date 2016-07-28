package amuse.utils;

import amuse.exceptions.TemplateLoadException;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public class TemplateLoader {

  private static TemplateLoader instance;

  private TemplateLoader() {  }

  /**
   * TODO: replace with proper singleton realization.
   * @return single initialized instance of the TemplateLoader.
   */
  public static TemplateLoader getInstance() {
    if (instance == null) {
      instance = new TemplateLoader();
    }
    return instance;
  }

  public <T> T loadTemplate(final String path) throws TemplateLoadException {
    final URL resource = getClass().getClassLoader().getResource(path);
    try {
      assert resource != null;
      return FXMLLoader.load(resource);
    } catch (IOException e) {
      throw new TemplateLoadException(path);
    }
  }
}
