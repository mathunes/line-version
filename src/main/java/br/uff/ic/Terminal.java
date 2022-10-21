package br.uff.ic;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class Terminal {
    
	private String directory;

	public Terminal() {
		this.directory = ".";
	}

    public List<String> runCommand(String command, String directory) {
		this.directory = directory;
		return this.runCommand(command);
	}

    public List<String> runCommand(String command) {
        List<String> outputLines = new ArrayList();

		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(command, null, new File(this.directory));
			process.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String outputLine;

			while ((outputLine = reader.readLine()) != null) {
                outputLines.add(outputLine);
			}

			reader.close();
			process.getOutputStream().close();

		} catch (Exception e) {
			e.printStackTrace();
		}

        return outputLines;
	}

}