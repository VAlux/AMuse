package amuse.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class APIProvider {

    private final String SCHEME = "https";
    private final String HOST = "api.vk.com";
    private final String PATH = "/method/audio.get";

    private URIBuilder builder;
    private boolean initialized;
    private static APIProvider instance;

    private APIProvider(){
        builder = new URIBuilder();
        builder.setScheme(SCHEME);
        builder.setHost(HOST);
        builder.setPath(PATH);
        initialized = false;
    }

    public void init(String userID, String accessToken){
        builder.setParameter("oid", userID);
        builder.setParameter("access_token", accessToken);
        initialized = true;
    }

    public static APIProvider getInstance(){
        if (instance == null)
            instance = new APIProvider();
        return instance;
    }

    public HttpEntity executeQuery() throws URISyntaxException, IOException {
        if (!initialized){
            System.err.println("Provider is not initialized. Use init() to fix.");
            return null;
        }
        URI uri = builder.build();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(uri);
        HttpResponse response = client.execute(get);
        return response.getEntity();
    }

    public void addParameter(String paramName, String paramValue) {
        this.builder.setParameter(paramName, paramValue);
    }
}
