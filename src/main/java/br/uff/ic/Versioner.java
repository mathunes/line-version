package br.uff.ic;

import java.io.File;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

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
                    refsJson.write("{\"objects\": []}");
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
                        refsJson.write("{\"objects\": []}");
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

    public void addFileToVersioning(String file) {
        if (new File(file).exists()) {
            //check if file is versioned by git
            if (git.lsFiles(file).size() > 0) {
                for (int i = 0; i < git.lsFiles(file).size(); i++) {
                    if (this.checkIfLvnObjectFromFileExists(git.lsFiles(file).get(i))) {
                        System.out.println("lvn: " + git.lsFiles(file).get(i) + " is already versioned.");
                    } else {
                        String lvnObjectName = this.createLvnObjectToFile(git.lsFiles(file).get(i));

                        if (lvnObjectName.isEmpty()) {
                            System.out.println("lvn: failed to create object to file: " + git.lsFiles(file).get(i));
                        } else {        
                            createVersioningForObjectFile(git.lsFiles(file).get(i), lvnObjectName);
                        }
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

    public String createLvnObjectToFile(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(".lvn/refs.json"));
            String refsJsonString = "";

            while (scanner.hasNext()){
                refsJsonString = refsJsonString + scanner.nextLine() + "\n";
            }

            scanner.close();
            
            JSONObject refsJsonObjects = new JSONObject(refsJsonString);
            JSONArray refsJsonObjectsArray = refsJsonObjects.getJSONArray("objects");
            
            UUID uuid = UUID.randomUUID();
            String lvnObjectName = uuid.toString();

            JSONObject newLvnObject = new JSONObject().put("path", filePath).put("object", lvnObjectName);
            
            refsJsonObjectsArray.put(newLvnObject);

            FileWriter refsJsonFile = new FileWriter(".lvn/refs.json");
            refsJsonFile.write("{\"objects\": " + refsJsonObjectsArray.toString(4) +  "}");
            refsJsonFile.close();

            new File(".lvn/objects/" + lvnObjectName + ".json").createNewFile();

            FileWriter objectJsonFile = new FileWriter(".lvn/objects/" + lvnObjectName + ".json");
            objectJsonFile.write("{\"lines\": []}");
            objectJsonFile.close();

            return lvnObjectName;

        } catch (Exception e) {
            System.out.println("lvn: " + e);
        }
        
        return "";
    }

    public List<CommitInfo> getCommitsInfoFromFile(String filePath) {
        List<String> logList = git.logReverse(filePath);
        List<CommitInfo> commitInfoList = new ArrayList();
        
        for (int i = 0; i < logList.size(); i++) {

            if (logList.get(i).startsWith("commit ")) {
                CommitInfo commitInfo = new CommitInfo();

                commitInfo.setHash(logList.get(i).replace("commit ", ""));
                commitInfo.setAuthor(logList.get(i + 1).replace("Author: ", ""));
                commitInfo.setDate(logList.get(i + 2).replace("Date:   ", ""));
                commitInfo.setMessage(logList.get(i + 4).trim());

                i+=5;

                commitInfoList.add(commitInfo);
            }
        }

        return commitInfoList;
    }

    public void createVersioningForObjectFile(String filePath, String objectName) {
        List<CommitInfo> commitInfoList = this.getCommitsInfoFromFile(filePath);

        String hash = "";
        String author = "";
        String date = "";
        String message = "";

        JSONObject objectJsonObjects;
        JSONArray objectJsonLinesArray;

        try {
            Scanner scanner = new Scanner(new File(".lvn/objects/" + objectName + ".json"));
            String objectJsonString = "";

            while (scanner.hasNext()){
                objectJsonString = objectJsonString + scanner.nextLine() + "\n";
            }

            scanner.close();

            objectJsonObjects = new JSONObject(objectJsonString);
            objectJsonLinesArray = objectJsonObjects.getJSONArray("lines");
        } catch (Exception e) {
            System.out.println("lvn: " + e);
            return;
        }

        //for each commit
        for (int i = 0; i < commitInfoList.size(); i++) {

            hash = commitInfoList.get(i).getHash();
            author = commitInfoList.get(i).getAuthor();
            date = commitInfoList.get(i).getDate();
            message = commitInfoList.get(i).getMessage();

            List<String> fileByCommit = git.showCompleteFileByCommit(filePath, hash);

            //first version
            if (objectJsonLinesArray.length() == 0) {
                
                //for each file line
                for (int j = 0; j < fileByCommit.size(); j++) {   
                    JSONObject lineObject = commitInfoList.get(i).getLineObject(fileByCommit.get(j));

                    JSONArray lineArray = new JSONArray("[" + lineObject + "]");

                    objectJsonLinesArray.put(lineArray.toString());
                }

            } else {
                int numberOfLinesAlreadyVersioned = objectJsonLinesArray.length();

                //for each file line
                for (int j = 0; j < fileByCommit.size(); j++) {
                    
                    if (j < numberOfLinesAlreadyVersioned) {
                        JSONArray objectJsonLineArray = new JSONArray(objectJsonLinesArray.getString(j));
                        String contentLine = objectJsonLineArray.getJSONObject(objectJsonLineArray.length() - 1).getString("content");
                        
                        if (!(fileByCommit.get(j).equals(contentLine))) {

                            JSONObject lineObject = commitInfoList.get(i).getLineObject(fileByCommit.get(j));

                            objectJsonLineArray.put(lineObject);

                            objectJsonLinesArray.put(j, objectJsonLineArray.toString());

                        }

                    }
                    
                    // 
                    //if fileByCommit size > numberOfLinesAlreadyVersioned then mark with DELETED
                }
            }

        }

        try {
            FileWriter objectJson = new FileWriter(".lvn/objects/" + objectName + ".json");
            objectJson.write("{\"lines\": [");

            for (int p = 0; p < objectJsonLinesArray.length(); p++) {
                objectJson.write(objectJsonLinesArray.getString(p));

                if (p < objectJsonLinesArray.length() - 1) {
                    objectJson.write(",");
                }
            }

            objectJson.write("]}");

            objectJson.close();
        } catch (Exception e) {
            System.out.println("lvn: " + e);
        }

        //get commit info from log (Add in CommitInfo class and append in a List<CommitInfo>)
        //for each commit
            //get complete content from file with git show <commit>:file-path
                //lvn object is void
                    //for each line from file
                        //put line in lvn object
                //lvn object is not void
                    //for each line from file
                        //if line is already in lvn object
                            //continue
                        //if line is replaced in lvn object
                            //append
    }
}