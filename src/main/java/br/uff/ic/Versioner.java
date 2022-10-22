package br.uff.ic;

import java.io.File;

public class Versioner {

    private Git git;
    private Terminal terminal;

    public Versioner() {
        git = new Git();
        terminal = new Terminal();
    }

    public void init() {
        if ((git.revParse().size() > 0) && (git.revParse().get(0).equals("true"))) {
            if (new File(".lvn").exists()) {
                System.out.println("lvn: repository is already initialized.");
            } else {
                terminal.runCommand("mkdir .lvn");
                System.out.println("lvn: initialized repository.");
            }
        } else {
            System.out.println("lvn: this is a not git repository.");
        }
    }

    public void init(String directory) {
        if (!(new File(directory).exists())) {
            System.out.println("lvn: this directory does not exists.");
        } else {    
            if ((git.revParse(directory).size() > 0) && (git.revParse(directory).get(0).equals("true"))) {
                if (new File(directory + "/.lvn").exists()) {
                    System.out.println("lvn: repository is already initialized.");
                } else {
                    terminal.runCommand("mkdir " + directory + "/.lvn");
                    System.out.println("lvn: initialized repository.");
                }
            } else {
                System.out.println("lvn: this is a not git repository.");
            }
        }   
    }

    public Boolean isLvnRepository() {
        if (new File(".lvn").exists()) {
            return true;
        }
        System.out.println("lvn: this is a not lvn repository.");
        return false;
    }

    public Boolean isLvnRepository(String directory) {
        if (new File(directory + "/.lvn").exists()) {
            return true;
        }
        System.out.println("lvn: this is a not lvn repository.");
        return false;
    }

    public void createVersion(String file) {
        if (new File(file).exists()) {
            if (git.lsFiles(file).size() > 0) {
                for (int i = 0; i < git.lsFiles(file).size(); i++) {
                    System.out.println(git.lsFiles(file).get(i));
                }
            }
        } else {
            System.out.println("lvn: this file does not exists.");
        }
    }
}
