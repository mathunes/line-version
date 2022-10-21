package br.uff.ic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

public class GitTest {

    @Test
    public void shouldRunGitRevParseInAGitRepository() {
        Terminal terminal = new Terminal();
        Git git = new Git();
        String directoryName = "shouldRunGitRevParseInAGitRepository";

        File directory = new File("../" + directoryName);
        if (!directory.exists()){
            directory.mkdirs();
        }

        terminal.runCommand("git init", "../" + directoryName);

        assertTrue(git.revParse("../" + directoryName).size() > 0);
        assertTrue(git.revParse("../" + directoryName).get(0).equals("true"));
        
        terminal.runCommand("rm -r .git", "../" + directoryName);

        directory.delete();
    }

    @Test
    public void shouldRunGitRevParseInNotGitRepository() {
        Terminal terminal = new Terminal();
        Git git = new Git();
        String directoryName = "shouldRunGitRevParseInAGitRepository";

        File directory = new File("../" + directoryName);
        if (!directory.exists()){
            directory.mkdirs();
        }

        assertTrue(git.revParse("../" + directoryName).size() == 0);
        
        directory.delete();
    }
}
