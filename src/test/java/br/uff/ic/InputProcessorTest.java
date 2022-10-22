package br.uff.ic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InputProcessorTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static InputProcessor inputProcessor;

    @BeforeAll
    static void initAll() {
        inputProcessor = new InputProcessor();
    }

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void shouldRunHandleInputWithoutArguments() {
        inputProcessor.handleInput(new String[]{});
        assertEquals("lvn: type a lvn command.", outputStreamCaptor.toString().trim());
    }

    @Test
    public void shouldRunHandleInputWithInvalidArgument() {
        String invalidParameter = "test";
        inputProcessor.handleInput(new String[]{invalidParameter});
        assertEquals("lvn: " + invalidParameter + " is not a lvn command.", outputStreamCaptor.toString().trim());
    }

    @Test
    public void shouldRunHandleInputWithValidArgument() {
        Terminal terminal = new Terminal();
        String directoryName = "shouldRunHandleInputWithValidArgument";

        File directory = new File("../" + directoryName);
        if (!directory.exists()){
            directory.mkdirs();
        }

        File lvnDir = new File("../" + directoryName + "/.lvn");
        if (lvnDir.exists()){   
            lvnDir.delete();
        }
     
        terminal.runCommand("git init", "../" + directoryName);

        String validParameter = "init";
        inputProcessor.handleInput(new String[]{validParameter, "../" + directoryName});
        assertEquals("lvn: initialized repository.", outputStreamCaptor.toString().trim());
     
        terminal.runCommand("rm -r .git", "../" + directoryName);

        lvnDir.delete();
        directory.delete();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
