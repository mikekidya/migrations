package migrations;

import java.io.InputStream;

import static migrations.api.NewStorageAPI.newStorageAPI;
import static migrations.api.OldStorageAPI.oldStorageAPI;

public class Migration {

    public static void migrateFile(String filename) {
        while (!newStorageAPI().getFiles().contains(filename)) {
            InputStream file = oldStorageAPI().getFile(filename);
            newStorageAPI().uploadFile(file, filename);
        }
        oldStorageAPI().deleteFile(filename);
    }

}
