package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.AffectedFile;
import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Util.RepoWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Controller
public class FileTreeControllerImpl implements FileTreeController
{
    @Override
    public FileTree buildFileTree(Path root, List<AffectedFile> affectedFiles)
    {
        return new FileTree(root.toString(), affectedFiles);
    }
}
