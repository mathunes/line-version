package br.uff.ic;

import java.util.List;

public class Git {
	
	private Terminal terminal;

	public Git() {
		terminal = new Terminal();
	}

	public List<String> revParse() {
		String gitCommand = "git rev-parse --is-inside-work-tree";
		return this.terminal.runCommand(gitCommand);
	}

	public List<String> revParse(String directory) {
		String gitCommand = "git rev-parse --is-inside-work-tree";
		return this.terminal.runCommand(gitCommand, directory);
	}

	public List<String> lsFiles(String filePath) {
		String gitCommand = "git ls-files " + filePath;
		return this.terminal.runCommand(gitCommand);
	}

	public List<String> logReverse(String filePath) {
		String gitCommand = "git log --reverse " + filePath;
		return this.terminal.runCommand(gitCommand);	
	}

	public List<String> showCompleteFileByCommit(String filePath, String commit) {
		String gitCommand = "git show " + commit + ":" + filePath;
		return this.terminal.runCommand(gitCommand);
	}

	public List<String> logPReverse(String filePath) {
		String gitCommand = "git log -p --reverse " + filePath;
		return this.terminal.runCommand(gitCommand);
	}

}