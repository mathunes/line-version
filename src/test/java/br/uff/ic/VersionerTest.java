package br.uff.ic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class VersionerTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static InputProcessor inputProcessor;
 
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Order(1)
    @Test
    public void shouldInitializeALvnRepositoryInAnExistingDirectory() {
        Terminal terminal = new Terminal();
        Versioner versioner = new Versioner();
        String directoryName = "shouldInitializeALvnRepositoryInAnExistingDirectory";

        File directory = new File("../" + directoryName);
        if (!directory.exists()){
            directory.mkdirs();
        }

        terminal.runCommand("git init", "../" + directoryName);
        versioner.init("../" + directoryName);
        assertEquals("lvn: initialized repository.", outputStreamCaptor.toString().trim());        
    }

    @Order(2)
    @Test
    public void shouldInitializeALvnRepositoryInAnExistingLvnRepository() {
        Terminal terminal = new Terminal();
        Versioner versioner = new Versioner();
        String directoryName = "shouldInitializeALvnRepositoryInAnExistingDirectory";

        File directory = new File("../" + directoryName);

        versioner.init("../" + directoryName);
        assertEquals("lvn: repository is already initialized.", outputStreamCaptor.toString().trim());
        
        terminal.runCommand("rm -r .git", "../" + directoryName);
        terminal.runCommand("rm -r .lvn", "../" + directoryName);

        directory.delete();
    }

    @Test
    public void shouldTryInitializeALvnRepositoryInAnNotExistingDirectory() {
        Versioner versioner = new Versioner();
        String directory = "shouldTryInitializeALvnRepositoryInAnNotExistingDirectory";
   
        versioner.init("../" + directory);
        assertEquals("lvn: this directory does not exists.", outputStreamCaptor.toString().trim());
    }

    @Test
    public void shouldTryInitializeALvnRepositoryInAnNotGitRepository() {
        Terminal terminal = new Terminal();
        Versioner versioner = new Versioner();
        String directoryName = "shouldTryInitializeALvnRepositoryInAnNotGitRepository";

        File directory = new File("../" + directoryName);
        if (!directory.exists()){
            directory.mkdirs();
        }

        versioner.init("../" + directoryName);
        assertEquals("lvn: this is a not git repository.", outputStreamCaptor.toString().trim());
        
        terminal.runCommand("rm -r .lvn", "../" + directoryName);

        directory.delete();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}