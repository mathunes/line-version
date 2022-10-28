package br.uff.ic;

import java.io.File;
import java.io.FileWriter;

import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONArray;

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
                terminal.runCommand("mkdir objects", ".lvn");

                try {
                    new File(".lvn/refs.json").createNewFile();

                    FileWriter refsJson = new FileWriter(".lvn/refs.json");
                    refsJson.write("{}");
                    refsJson.close();
                } catch (Exception e) {
                    System.out.println("lvn: failed to create refs.json file.");
                }

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
                    terminal.runCommand("mkdir objects", directory + "/.lvn");

                    try {
                        new File(directory + "/.lvn/refs.json").createNewFile();

                        FileWriter refsJson = new FileWriter(directory + "/.lvn/refs.json");
                        refsJson.write("{}");
                        refsJson.close();
                    } catch (Exception e) {
                        System.out.println("lvn: failed to create refs.json file.");
                    }

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
            //check if file is versioned by git
            if (git.lsFiles(file).size() > 0) {
                for (int i = 0; i < git.lsFiles(file).size(); i++) {
                    //use git log -p --reverse <file>
                    // System.out.println(git.lsFiles(file).get(i));
                    if (this.checkIfLvnObjectFromFileExists(file)) {
                        System.out.println("Exists");
                    } else {
                        System.out.println("Not exists");
                    }
                }
            }
        } else {
            System.out.println("lvn: this file does not exists.");
        }
    }

    public boolean checkIfLvnObjectFromFileExists(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(".lvn/refs.json"));
            String refsJsonString = "";

            while (scanner.hasNext()){
                refsJsonString = refsJsonString + scanner.nextLine() + "\n";
            }

            scanner.close();
            
            JSONObject refsJsonObjects = new JSONObject(refsJsonString);
            JSONArray refsJsonObjectsArray = refsJsonObjects.getJSONArray("objects");

            for (int i = 0; i < refsJsonObjectsArray.length(); i++) {
                JSONObject refFile = refsJsonObjectsArray.getJSONObject(i);
                
                if (filePath.equals(refFile.get("path"))) {
                    if (new File(".lvn/objects/" + refFile.get("object") + ".json").exists()) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("lvn: " + e);
        }

        return false;
    }
}
