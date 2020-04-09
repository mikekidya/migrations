package migrations.api;

import java.io.InputStream;
import java.util.List;

import static migrations.api.CommonStorageAPI.commonApi;

public class NewStorageAPI {

    private static final String PATH = "newStorage";

    private static NewStorageAPI storageAPI = null;

    public static NewStorageAPI newStorageAPI() {
        if (storageAPI == null) {
            storageAPI = new NewStorageAPI();
        }
        return storageAPI;
    }

    public List<String> getFiles() {
        return commonApi().getFiles(PATH);
    }

    public InputStream getFile(String filename) {
        return commonApi().getFile(PATH, filename);
    }

    public boolean isFilePresent(String filename) {
        return commonApi().isFilePresent(PATH, filename);
    }

    public void uploadFile(InputStream file, String filename) {
        commonApi().uploadFile(PATH, file, filename);
    }

    public void deleteFile(String filename) {
        commonApi().deleteFile(PATH, filename);
    }


}
