package de.dickert.reporisktool.Exception;

public class JiraException extends RuntimeException
{
    public JiraException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
