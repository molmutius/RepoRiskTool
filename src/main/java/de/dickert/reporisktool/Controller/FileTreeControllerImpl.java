package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoFile;
import de.dickert.reporisktool.Model.FileTree;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.util.List;

@Primary
@Controller
public class FileTreeControllerImpl implements FileTreeController
{
    @Override
    public FileTree buildFileTree(Path root, List<RepoFile> repoFiles, List<String> excludeNames)
    {
        return new FileTree(root.toString(), repoFiles, excludeNames);
    }
}
