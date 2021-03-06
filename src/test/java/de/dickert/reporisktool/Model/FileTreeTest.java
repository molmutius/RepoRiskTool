package de.dickert.reporisktool.Model;

import de.dickert.reporisktool.Util.NodeVisitor;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileTreeTest
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

    @BeforeMethod
    public void createTestDir() throws IOException
    {
        Files.createDirectories(folderRoot);
        Files.createDirectories(folderDir1);
        Files.createDirectories(folderDir2);
        Files.createFile(fileRoot);
        Files.createFile(file1Dir1);
        Files.createFile(file2Dir1);
        Files.createFile(file1Dir2);
    }

    @AfterMethod
    public void deleteTestDir() throws IOException
    {
        Files.deleteIfExists(fileRoot);
        Files.deleteIfExists(file1Dir1);
        Files.deleteIfExists(file1Dir2);
        Files.deleteIfExists(file2Dir1);
        Files.deleteIfExists(folderDir1);
        Files.deleteIfExists(folderDir2);
        Files.deleteIfExists(folderRoot);
    }

    @Test
    public void TestGetIssueOfSubtree()
    {
        final RepoFile affectedFile1 = new RepoFile(file1Dir2.toFile(),
                Arrays.asList(new Issue("PAL-1234"), new Issue("PAL-555")));
        final RepoFile affectedFile2 = new RepoFile(fileRoot.toFile(),
                Arrays.asList(new Issue("PAL-999"), new Issue("PAL-555")));
        final List<RepoFile> repoFiles = Arrays.asList(affectedFile1, affectedFile2);
        final List<String> excludeDirs = Collections.emptyList();

        final FileTree fileTree = new FileTree(WORKING_DIR + TEST_ROOT_DIR, repoFiles, excludeDirs);
        final List<Issue> issuesUnderRoot = fileTree.getIssuesOfSubtree(folderRoot);
        final List<Issue> issuesDir2 = fileTree.getIssuesOfSubtree(folderDir2);
        final List<Issue> issuesDir1 = fileTree.getIssuesOfSubtree(folderDir1);

        Assert.assertEquals(issuesUnderRoot.size(), 4);
        Assert.assertEquals(issuesDir2.size(), 2);
        Assert.assertEquals(issuesDir1.size(), 0);
        Assert.assertTrue(issuesDir2.stream().anyMatch(issue -> issue.getTicketNumber().equals("PAL-1234")));
        Assert.assertTrue(issuesDir2.stream().anyMatch(issue -> issue.getTicketNumber().equals("PAL-555")));
        Assert.assertFalse(issuesDir2.stream().anyMatch(issue -> issue.getTicketNumber().equals("PAL-999")));
    }

    @Test
    public void TestExcludeDir()
    {
        final List<RepoFile> repoFiles = Collections.emptyList();
        final String dirToExclude = DIR_1.replace("/", "");
        final List<String> excludeDirs = Collections.singletonList(dirToExclude);
        final FileTree fileTree = new FileTree(WORKING_DIR + TEST_ROOT_DIR, repoFiles, excludeDirs);
        fileTree.traverseTree(new NodeVisitor()
        {
            @Override
            public void onFile(RepoFile file) { }

            @Override
            public void onDirectory(RepoDir directory)
            {
                Assert.assertNotEquals(directory, dirToExclude, "Expected directory to not be included.");
            }
        });
    }
}
