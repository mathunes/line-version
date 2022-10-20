package br.uff.ic;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class Git {
		
	public static List<String> revParse() {
		String gitCommand = "git rev-parse --is-inside-work-tree";
		return Terminal.runCommand(gitCommand);
	}

}