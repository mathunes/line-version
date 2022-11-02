package br.uff.ic;

import java.util.List;

public class Git {
		
	public List<String> revParse() {
		Terminal terminal = new Terminal();
		String gitCommand = "git rev-parse --is-inside-work-tree";
		return terminal.runCommand(gitCommand);
	}

	public List<String> revParse(String directory) {
		Terminal terminal = new Terminal();
		String gitCommand = "git rev-parse --is-inside-work-tree";
		return terminal.runCommand(gitCommand, directory);
	}

	public List<String> lsFiles(String filePath) {
		Terminal terminal = new Terminal();
		String gitCommand = "git ls-files " + filePath;
		return terminal.runCommand(gitCommand);
	}

	public List<String> logPReverse(String filePath) {
		Terminal terminal = new Terminal();
		String gitCommand = "git log -p --reverse " + filePath;
		return terminal.runCommand(gitCommand);	
	}

}