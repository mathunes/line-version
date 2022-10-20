package br.uff.ic;

import java.io.File;

public class Versioner {
    
    public void init() {

        if (new File(".git").exists()) {
            System.out.println("Initialized lvn repository in...");
        } else {
            System.out.println("lvn: this is a not git repository.");
        }
        
    }

}
