package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.AffectedFile;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RepoControllerImpl implements RepoController
{
    @Override
    public List<AffectedFile> getAffectedFiles(String directory, String startDate, String endDate, String projectDirectory)
    {
        return null;
    }
}
