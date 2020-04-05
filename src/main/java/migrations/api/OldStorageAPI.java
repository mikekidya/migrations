package migrations.api;

import java.io.InputStream;
import java.util.List;

import static migrations.api.CommonStorageAPI.commonApi;

public class OldStorageAPI {

    private static final String PATH = "oldStorage";

    private static OldStorageAPI storageAPI = null;

    public static OldStorageAPI oldStorageAPI() {
        if (storageAPI == null) {
            storageAPI = new OldStorageAPI();
        }
        return storageAPI;
    }

    public List<String> getFiles() {
        return commonApi().getFiles(PATH);
    }

    public InputStream getFile(String filename) {
        return commonApi().getFile(PATH, filename);
    }

    public void deleteFile(String filename) {
        commonApi().deleteFile(PATH, filename);
    }


}
