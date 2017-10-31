package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoFile;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@Controller
public class RepoControllerImpl implements RepoController
{
    @Override
    public List<RepoFile> getAffectedFiles(String directory, String startDate, String endDate, String projectDirectory)
    {
        return Collections.emptyList();
    }
}
