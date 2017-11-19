package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoItem;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@Controller
public class RepoControllerImpl implements RepoController
{
    @Override
    public List<RepoItem> getAffectedFiles(String directory, String startDate, String endDate, String projectDirectory)
    {
        return Collections.emptyList();
    }
}
