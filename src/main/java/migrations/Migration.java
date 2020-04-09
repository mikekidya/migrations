package migrations;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

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
        if (!newStorageAPI().isFilePresent(filename)) {
            try {
                String file = IOUtils.toString(oldStorageAPI().getFile(filename), Charset.defaultCharset());
                do {
                    newStorageAPI().uploadFile(IOUtils.toInputStream(file, Charset.defaultCharset()), filename);
                } while (!newStorageAPI().isFilePresent(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        oldStorageAPI().deleteFile(filename);
    }

}
