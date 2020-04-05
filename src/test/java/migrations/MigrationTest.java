package migrations;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static migrations.Migration.migrateFile;
import static migrations.api.NewStorageAPI.newStorageAPI;
import static migrations.api.OldStorageAPI.oldStorageAPI;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class MigrationTest {

    private List<String> oldFiles;

    @Before
    public void setUp() {
        oldFiles = oldStorageAPI().getFiles();
        assumeThat("No files to transfer", oldFiles, not(empty()));
    }

    @Test
    public void shouldDeleteFileAfterTransfer() {
        String filename = oldFiles.get(0);
        migrateFile(filename);
        oldFiles = oldStorageAPI().getFiles();
        assertThat("File is not deleted", oldFiles, not(hasItem(filename)));
    }

    @Test
    public void shouldNewHaveFileAfterTransfer() {
        String filename = oldFiles.get(0);
        migrateFile(filename);
        List<String> newFiles = newStorageAPI().getFiles();
        assertThat("File is not added", newFiles, hasItem(filename));
    }

    @Test
    public void shouldBeEqualAfterTransfer() throws IOException {
        String filename = oldFiles.get(0);
        String oldFile = IOUtils.toString(oldStorageAPI().getFile(filename), Charset.defaultCharset());
        migrateFile(filename);
        String newFile = IOUtils.toString(newStorageAPI().getFile(filename), Charset.defaultCharset());
        assertThat("Files are not equal", newFile, equalTo(oldFile));
    }

}