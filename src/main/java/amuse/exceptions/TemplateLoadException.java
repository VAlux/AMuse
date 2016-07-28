package amuse.exceptions;

public class TemplateLoadException extends Exception {
    public TemplateLoadException(String message) {
        super("Template loading error: " + message);
    }
}
