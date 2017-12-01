package de.dickert.reporisktool;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RepoRiskTool implements CommandLineRunner
{
    @Override
    public void run(String... args) throws Exception
    {

    }

    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(RepoRiskTool.class, args);
    }
}

