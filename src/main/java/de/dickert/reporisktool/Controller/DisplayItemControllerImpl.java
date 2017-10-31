package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoFile;
import de.dickert.reporisktool.Model.DisplayItem;
import de.dickert.reporisktool.Model.FileTree;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DisplayItemControllerImpl implements DisplayItemController
{
    @Override
    public List<DisplayItem> getItemsToDisplay(List<RepoFile> repoFiles, int maxDepth)
    {
        return null;
    }

    @Override
    public FileTree getFileTree(List<RepoFile> repoFiles)
    {
        return null;
    }
}
