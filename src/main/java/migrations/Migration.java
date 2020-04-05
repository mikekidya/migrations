package migrations;

import java.io.InputStream;

import static migrations.api.NewStorageAPI.newStorageAPI;
import static migrations.api.OldStorageAPI.oldStorageAPI;

public class Migration {

    /**
     * Transfers file from old storage to new one.
     * File will bw deleted from old storage after
     * successful transfer.
     *
     * @param filename name of file to be transferred.
     */
    public static void migrateFile(String filename) {
        while (!newStorageAPI().getFiles().contains(filename)) {
            InputStream file = oldStorageAPI().getFile(filename);
            newStorageAPI().uploadFile(file, filename);
        }
        oldStorageAPI().deleteFile(filename);
    }

}
