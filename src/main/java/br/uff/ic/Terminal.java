package br.uff.ic;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class Terminal {
    
    public static List<String> runCommand(String command) {
        List<String> outputLines = new ArrayList();

		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(command, null, new File("."));
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