package migrations;

import java.util.List;

import static migrations.Migration.migrateFile;
import static migrations.api.OldStorageAPI.oldStorageAPI;

public class Main {

    public static void main(String[] args) {
        List<String> files = oldStorageAPI().getFiles();
        System.out.println(String.format("Migration started: %d files to transfer", files.size()));

        int commonFilesNumber = files.size();
        int filesTransferred = 0;

        while (!files.isEmpty()) {
            for (String filename : files) {
                migrateFile(filename);
                filesTransferred++;
                if (filesTransferred % 20 == 0) {
                    System.out.println(String.format("Files transferred: %d/%d (%d %%)",
                            filesTransferred, commonFilesNumber, (100 * filesTransferred) / commonFilesNumber));
                }
            }
            files = oldStorageAPI().getFiles();
        }

        System.out.println("Finished");
    }
}
