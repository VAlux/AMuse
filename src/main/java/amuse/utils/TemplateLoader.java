package amuse.utils;

import amuse.exceptions.TemplateNotFoundException;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public class TemplateLoader {

    public <T> T loadTemplate(final String path) throws TemplateNotFoundException {
        final URL resource = getClass().getClassLoader().getResource(path);
        try {
            if (resource != null) {
                return FXMLLoader.load(resource);
            } else {
                throw new TemplateNotFoundException(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
