package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.Issue;
import de.dickert.reporisktool.Service.JiraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@Controller
public class JiraControllerImpl implements JiraController
{
    private Logger log = LoggerFactory.getLogger(getClass());

    @Value("${jira.url}")
    private String jiraBasePath;

    @Autowired
    private JiraService jiraService;

    @Override
    public List<Issue> getIssues(String projectName)
    {
        log.info("Getting Issues from Jira");
        String issuesJSON = jiraService.getIssuesJson();
        List<Issue> issues = parseIssues(issuesJSON);
        return issues;
    }

    /**
     * Takes the issues JSON String as returned from JIRA
     * and parses it into a list of the {@link Issue} data structure.
     * @param issuesJSON
     * @return a list of {@link Issue}s
     */
    private List<Issue> parseIssues(String issuesJSON)
    {
        return Collections.EMPTY_LIST;
    }
}
