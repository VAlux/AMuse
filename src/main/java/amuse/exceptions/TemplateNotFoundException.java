package amuse.exceptions;

/**
 * Created by ovoievodin on 6/3/2016.
 */
public class TemplateNotFoundException extends Exception {
    public TemplateNotFoundException(String message) {
        super("Template not fonud for: " + message);
    }
}
