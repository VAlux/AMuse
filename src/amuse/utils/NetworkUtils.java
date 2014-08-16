package amuse.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    public static int getContentSize(final URL url) throws IOException {
        HttpURLConnection connection;
        HttpURLConnection.setFollowRedirects(false);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        return connection.getContentLength();
    }
}
