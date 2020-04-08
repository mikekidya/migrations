package migrations.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

class CommonStorageAPI {

    private static final String PATH_TO_RES = "src/main/resources/api.properties";
    private static final int RETRY_COUNT = 10;

    private static CommonStorageAPI commonAPI = null;

    private HttpClient client;
    private String host;

    protected CommonStorageAPI() {
        loadProps();
        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(RETRY_COUNT, false);
        HttpResponseInterceptor responseInterceptor = (response, context) -> {
            int code = response.getStatusLine().getStatusCode();
            if (code >= 500) {
                throw new IOException(String.format("Server error: %d. Retry", code));
            }
        };
        client = HttpClientBuilder.create()
                .setRetryHandler(retryHandler)
                .addInterceptorLast(responseInterceptor)
                .build();

    }

    public static CommonStorageAPI commonApi() {
        if (commonAPI == null) {
            commonAPI = new CommonStorageAPI();
        }
        return commonAPI;
    }

    public List<String> getFiles(String storage) {
        try {
            String path = String.format("%s/%s/files", host, storage);
            HttpGet request = new HttpGet(path);
            request.addHeader("accept", "application/json");
            HttpResponse response = client.execute(request);
            checkCode(response);
            String stringResponse = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            return new JSONArray(stringResponse).toList().stream().map(o -> (String) o).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getFile(String storage, String filename) {
        String path = String.format("%s/%s/files/%s", host, storage, filename);
        HttpGet request = new HttpGet(path);
        request.addHeader("accept", "application/octet-stream");
        try {
            HttpResponse response = client.execute(request);
            checkCode(response);
            return response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isFilePresent(String storage, String filename) {
        String path = String.format("%s/%s/files/%s", host, storage, filename);
        HttpHead request = new HttpHead(path);
        try {
            HttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void uploadFile(String storage, InputStream file, String filename) {
        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, filename)
                .build();
        String path = String.format("%s/%s/files", host, storage);
        HttpPost request = new HttpPost(path);
        request.setEntity(entity);
        try {
            HttpResponse response = client.execute(request);
            checkCode(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteFile(String storage, String filename) {
        String path = String.format("%s/%s/files/%s", host, storage, filename);
        HttpDelete request = new HttpDelete(path);
        try {
            HttpResponse response = client.execute(request);
            checkCode(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadProps() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(PATH_TO_RES));

            host = properties.getProperty("api.host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkCode(HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            System.out.println(String.format("Request finished with code %d", statusCode));
        }
    }

}
