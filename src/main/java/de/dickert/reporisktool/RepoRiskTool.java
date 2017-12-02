package de.dickert.reporisktool;

import de.dickert.reporisktool.Model.RepoItem;
import de.dickert.reporisktool.Service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RepoRiskTool implements CommandLineRunner
{
    @Qualifier("GitRepoServiceImpl")
    @Autowired
    RepoService repoService;

    @Override
    public void run(String... args) throws Exception
    {
        List<RepoItem> repoItemList = repoService.getAffectedItems();
        System.out.println(repoItemList);
    }

    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(RepoRiskTool.class, args);
    }
}

