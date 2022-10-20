package br.uff.ic;

import java.io.File;

public class Versioner {

    private Git git;

    public Versioner() {
        git = new Git();
    }

    public void init() {
        if ((git.revParse().size() > 0) && (git.revParse().get(0).equals("true"))) {
            System.out.println("Initialized lvn repository.");
        } else {
            System.out.println("lvn: this is a not git repository.");
        }
    }

}
