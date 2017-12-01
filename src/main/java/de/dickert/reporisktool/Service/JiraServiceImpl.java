package de.dickert.reporisktool.Service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import de.dickert.reporisktool.Configuration.Properties;
import de.dickert.reporisktool.Model.RepoIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class JiraServiceImpl implements JiraService
{
    private Logger log = LoggerFactory.getLogger(getClass());

    private JiraRestClient jiraRestClient;
    private Properties properties;
    private String jiraUrl;

    @Autowired
    public JiraServiceImpl(Properties properties)
    {
        this.properties = properties;
        this.jiraUrl = properties.getJiraUrl();
        this.jiraRestClient = getJiraRestClient();
    }

    @Override
    public List<RepoIssue> getIssues()
    {
        final List<RepoIssue> issues = new ArrayList<>();
        final Promise<SearchResult> searchResult = jiraRestClient.getSearchClient().searchJql(jqlQueryGetAllIssues());
        for (Issue issue : searchResult.claim().getIssues())
        {
            issues.add(toRepoIssue(issue));
        }
        closeJiraRestClient();
        return issues;
    }

    private void closeJiraRestClient()
    {
        try
        {
            jiraRestClient.close();
        } catch (IOException e)
        {
            log.error("Could not close Jira Rest Client", e);
        }
    }

    private RepoIssue toRepoIssue(Issue issue)
    {
        return new RepoIssue(issue);
    }

    private JiraRestClient getJiraRestClient()
    {
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        jiraRestClient = factory.createWithBasicHttpAuthentication(
                getJiraUri(), properties.getJiraUser(), properties.getJiraPassword());

        return jiraRestClient;
    }

    private URI getJiraUri()
    {
        return URI.create(this.jiraUrl);
    }

    // TODO: Return issues of more than 1 project
    private String jqlQueryGetAllIssues()
    {
        return String.format(String.format("project=%s", properties.getProjectName()));
    }
}
