package de.dickert.reporisktool.Service;

import de.dickert.reporisktool.Model.RepoItem;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@Controller
public class RepoServiceImpl implements RepoService
{
    @Override
    public List<RepoItem> getAffectedFiles(String directory, String startDate, String endDate, String projectDirectory)
    {
        return Collections.emptyList();
    }
}
