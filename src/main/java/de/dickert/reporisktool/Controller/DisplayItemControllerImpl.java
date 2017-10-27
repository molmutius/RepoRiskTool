package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.AffectedFile;
import de.dickert.reporisktool.Model.DisplayItem;
import de.dickert.reporisktool.Model.FileTree;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DisplayItemControllerImpl implements DisplayItemController
{
    @Override
    public List<DisplayItem> getItemsToDisplay(List<AffectedFile> affectedFiles, int maxDepth)
    {
        return null;
    }

    @Override
    public FileTree getFileTree(List<AffectedFile> affectedFiles)
    {
        return null;
    }
}
