package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoFile;
import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Model.RepoItem;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DisplayItemControllerImpl implements DisplayItemController
{
    @Override
    public List<RepoItem> getItemsToDisplay(List<RepoFile> repoFiles, int maxDepth)
    {
        return null;
    }

    @Override
    public FileTree getFileTree(List<RepoFile> repoFiles)
    {
        return null;
    }
}
