package migrations;

import java.util.List;

import static migrations.Migration.migrateFile;
import static migrations.api.OldStorageAPI.oldStorageAPI;

public class Main {

    public static void main(String[] args) {
        List<String> files = oldStorageAPI().getFiles();
        System.out.println(String.format("Migration started: %d files to transfer", files.size()));

        for (int i = 0; i < files.size(); i++) {
            String filename = files.get(i);
            migrateFile(filename);
            if (i % 20 == 0) {
                System.out.println(String.format("Files transferred: %d/%d (%d %%)",
                        i + 1, files.size(), (100 * i) / files.size()));
            }
        }

        System.out.println("Finished");
    }
}
