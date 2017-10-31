package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Model.Issue;
import de.dickert.reporisktool.Model.RepoFile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@Qualifier("DummyFileTreeControllerImpl")
public class DummyFileTreeControllerImpl implements FileTreeController
{
    private static final String DIR_1 = "/dir1";
    private static final String DIR_2 = "/dir2";
    private static final String FILE_ROOT = "/file_root.txt";
    private static final String FILE1_DIR1 = "/file1_dir1.txt";
    private static final String FILE2_DIR1 = "/file2_dir1.txt";
    private static final String FILE1_DIR2 = "/file1_dir2.txt";
    private static final String WORKING_DIR = Paths.get("").toAbsolutePath().normalize().toString();
    private static final String TEST_ROOT_DIR = "/temp_test";

    private Path folderRoot = Paths.get(WORKING_DIR + TEST_ROOT_DIR);
    private Path folderDir1 = Paths.get(WORKING_DIR + TEST_ROOT_DIR + DIR_1);
    private Path folderDir2 = Paths.get(WORKING_DIR + TEST_ROOT_DIR + DIR_2);
    private Path fileRoot = Paths.get(WORKING_DIR + TEST_ROOT_DIR + FILE_ROOT);
    private Path file1Dir1 = Paths.get(WORKING_DIR + TEST_ROOT_DIR + DIR_1 + FILE1_DIR1);
    private Path file2Dir1 = Paths.get(WORKING_DIR + TEST_ROOT_DIR + DIR_1 + FILE2_DIR1);
    private Path file1Dir2 = Paths.get(WORKING_DIR + TEST_ROOT_DIR + DIR_2 + FILE1_DIR2);

    private void createDirsAndFiles()
    {
        try
        {
            Files.createDirectories(folderRoot);
            Files.createDirectories(folderDir1);
            Files.createDirectories(folderDir2);
            Files.createFile(fileRoot);
            Files.createFile(file1Dir1);
            Files.createFile(file2Dir1);
            Files.createFile(file1Dir2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void deleteDirsAndFiles()
    {
        try
        {
            Files.deleteIfExists(fileRoot);
            Files.deleteIfExists(file1Dir1);
            Files.deleteIfExists(file1Dir2);
            Files.deleteIfExists(file2Dir1);
            Files.deleteIfExists(folderDir1);
            Files.deleteIfExists(folderDir2);
            Files.deleteIfExists(folderRoot);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public FileTree buildFileTree(Path root, List<RepoFile> repoFiles, List<String> excludeNames)
    {
        createDirsAndFiles();
        final RepoFile affectedFile1 = new RepoFile(file1Dir2.toFile(),
                Arrays.asList(new Issue("PAL-1234"), new Issue("PAL-555")));
        final RepoFile affectedFile2 = new RepoFile(fileRoot.toFile(),
                Arrays.asList(new Issue("PAL-999"), new Issue("PAL-555")));
        final List<RepoFile> affectedFiles = Arrays.asList(affectedFile1, affectedFile2);
        final List<String> excludeDirs = Collections.emptyList();
        final FileTree fileTree = new FileTree(WORKING_DIR + TEST_ROOT_DIR, affectedFiles, excludeDirs);
        // We've already saved the file tree to memory, so it's save to delete the actual directory structure
        deleteDirsAndFiles();
        return fileTree;
    }
}
