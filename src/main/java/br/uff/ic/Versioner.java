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
    private static String TAG_LVN_DELETED = "<lvn>DELETED</lvn>";

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
                int numberOfCommits = Integer.parseInt(terminal.runCommand("git rev-list --all --count").get(0));

                try {
                    new File(".lvn/refs.json").createNewFile();

                    FileWriter refsJson = new FileWriter(".lvn/refs.json");
                    refsJson.write("{\"objects\": [], \"number-of-commits\":" + numberOfCommits + "}");
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
                    int numberOfCommits = Integer.parseInt(terminal.runCommand("git rev-list --all --count").get(0));

                    try {
                        new File(directory + "/.lvn/refs.json").createNewFile();

                        FileWriter refsJson = new FileWriter(directory + "/.lvn/refs.json");
                        refsJson.write("{\"objects\": [], \"number-of-commits\":" + numberOfCommits + "}");
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
            int numberOfCommits = refsJsonObjects.getInt("number-of-commits");
            
            UUID uuid = UUID.randomUUID();
            String lvnObjectName = uuid.toString();

            JSONObject newLvnObject = new JSONObject().put("path", filePath).put("object", lvnObjectName);
            
            refsJsonObjectsArray.put(newLvnObject);

            FileWriter refsJsonFile = new FileWriter(".lvn/refs.json");
            refsJsonFile.write("{\"objects\": " + refsJsonObjectsArray.toString(4) +  ", \"number-of-commits\": " + numberOfCommits + "}");
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
                    } else {
                        JSONObject lineObject = commitInfoList.get(i).getLineObject(fileByCommit.get(j));

                        JSONArray lineArray = new JSONArray("[" + lineObject + "]");
    
                        objectJsonLinesArray.put(lineArray.toString());
                    }
                }

               
                if (fileByCommit.size() < numberOfLinesAlreadyVersioned) {
                    for (int j = fileByCommit.size(); j < numberOfLinesAlreadyVersioned; j++) {
                        JSONArray lineArray = new JSONArray(objectJsonLinesArray.getString(j));

                        if (!(lineArray.getJSONObject(lineArray.length() - 1)).get("content").equals(TAG_LVN_DELETED)) {
                            JSONObject lineObject = commitInfoList.get(i).getLineObject(TAG_LVN_DELETED);
                        
                            lineArray.put(lineObject);

                            objectJsonLinesArray.put(j, lineArray.toString());
                        }
                    }
                }
            }
        }

        try {
            FileWriter objectJson = new FileWriter(".lvn/objects/" + objectName + ".json");
            objectJson.write("{\"lines\": [");

            for (int k = 0; k < objectJsonLinesArray.length(); k++) {
                objectJson.write(objectJsonLinesArray.getString(k));

                if (k < objectJsonLinesArray.length() - 1) {
                    objectJson.write(",");
                }
            }

            objectJson.write("]}");
            objectJson.close();

            System.out.println("lvn: versioning created for " + filePath);
        } catch (Exception e) {
            System.out.println("lvn: " + e);
        }
    }

    public String getLvnObjectFromFile(String filePath) {
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
                        return refFile.get("object").toString();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("lvn: " + e);
        }

        return "";
    }

    public void getLinesInfoFromFile(String filePath) {

        if (new File(filePath).exists()) {
            if (this.checkIfLvnObjectFromFileExists(filePath)) {

                String objectName = this.getLvnObjectFromFile(filePath);

                System.out.println(objectName);

                //Create method for this
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

                JSONArray jsonLineArray;
                JSONObject jsonVersion;

                for (int i = 0; i < objectJsonLinesArray.length(); i++) {
                    
                    jsonLineArray = new JSONArray(objectJsonLinesArray.getJSONArray(i));

                    System.out.println("LINE " + (i+1) + ":");
                    for (int j = 0; j < jsonLineArray.length(); j++) {
                        System.out.println("\tVERSION " + (j+1) + ": ");

                        System.out.println("\t\tCONTENT: " + jsonLineArray.getJSONObject(j).getString("content"));
                        System.out.println("\t\tAUTHOR: " + jsonLineArray.getJSONObject(j).getString("author"));
                        System.out.println("\t\tDATE: " + jsonLineArray.getJSONObject(j).getString("date"));
                        System.out.println("\t\tMESSAGE: " + jsonLineArray.getJSONObject(j).getString("message"));
                        System.out.println("\t\tHASH COMMIT: " + jsonLineArray.getJSONObject(j).getString("hash"));

                    }
                }
            } else {
                System.out.println("lvn: this file is not versioned by lvn.");
            }
        } else {
            System.out.println("lvn: this file does not exists.");
        }
    }

    public void getLineInfoFromFile(String filePath, int lineNumber) {
        if (new File(filePath).exists()) {
            if (this.checkIfLvnObjectFromFileExists(filePath)) {

                String objectName = this.getLvnObjectFromFile(filePath);

                //Create method for this
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

                JSONArray jsonLineArray;
                JSONObject jsonVersion;

                if ((lineNumber >= 0) && (lineNumber < objectJsonLinesArray.length())) {    
                    jsonLineArray = new JSONArray(objectJsonLinesArray.getJSONArray(lineNumber));

                    System.out.println("LINE " + (lineNumber+1) + ":");
                    for (int j = 0; j < jsonLineArray.length(); j++) {
                        System.out.println("\tVERSION " + (j+1) + ": ");

                        System.out.println("\t\tCONTENT: " + jsonLineArray.getJSONObject(j).getString("content"));
                        System.out.println("\t\tAUTHOR: " + jsonLineArray.getJSONObject(j).getString("author"));
                        System.out.println("\t\tDATE: " + jsonLineArray.getJSONObject(j).getString("date"));
                        System.out.println("\t\tMESSAGE: " + jsonLineArray.getJSONObject(j).getString("message"));
                        System.out.println("\t\tHASH COMMIT: " + jsonLineArray.getJSONObject(j).getString("hash"));

                    }
                } else {
                    System.out.println("lvn: invalid line number.");
                }
            } else {
                System.out.println("lvn: this file is not versioned by lvn.");
            }
        } else {
            System.out.println("lvn: this file does not exists.");
        }        
    }

    public void getLastLineVersionInfoFromFile(String filePath, int lineNumber) {
        if (new File(filePath).exists()) {
            if (this.checkIfLvnObjectFromFileExists(filePath)) {

                String objectName = this.getLvnObjectFromFile(filePath);

                //Create method for this
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

                JSONArray jsonLineArray;
                JSONObject jsonVersion;

                if ((lineNumber >= 0) && (lineNumber < objectJsonLinesArray.length())) {    
                    jsonLineArray = new JSONArray(objectJsonLinesArray.getJSONArray(lineNumber));

                    System.out.println("LINE " + (lineNumber+1) + ":");
                    
                    int lastLineVersion = jsonLineArray.length() - 1;

                    System.out.println("\tVERSION " + (lastLineVersion + 1) + ": ");

                    System.out.println("\t\tCONTENT: " + jsonLineArray.getJSONObject(lastLineVersion).getString("content"));
                    System.out.println("\t\tAUTHOR: " + jsonLineArray.getJSONObject(lastLineVersion).getString("author"));
                    System.out.println("\t\tDATE: " + jsonLineArray.getJSONObject(lastLineVersion).getString("date"));
                    System.out.println("\t\tMESSAGE: " + jsonLineArray.getJSONObject(lastLineVersion).getString("message"));
                    System.out.println("\t\tHASH COMMIT: " + jsonLineArray.getJSONObject(lastLineVersion).getString("hash"));

                } else {
                    System.out.println("lvn: invalid line number.");
                }
            } else {
                System.out.println("lvn: this file is not versioned by lvn.");
            }
        } else {
            System.out.println("lvn: this file does not exists.");
        }        
    }

    public void updateObjects() {
        try {
            Scanner scanner = new Scanner(new File(".lvn/refs.json"));
            String refsJsonString = "";

            while (scanner.hasNext()){
                refsJsonString = refsJsonString + scanner.nextLine() + "\n";
            }

            scanner.close();
            
            JSONObject refsJsonObjects = new JSONObject(refsJsonString);
            JSONArray refsJsonObjectsArray = refsJsonObjects.getJSONArray("objects");
            int numberOfCommitsInRefs = refsJsonObjects.getInt("number-of-commits");

            int numberOfCommits = Integer.parseInt(terminal.runCommand("git rev-list --all --count").get(0));

            if (numberOfCommitsInRefs != numberOfCommits) {
                //delete all objects
                terminal.runCommand("rm -rf .lvn/objects/*");

                try {
                    FileWriter refsJson = new FileWriter(".lvn/refs.json");
                    refsJson.write("{\"objects\": [], \"number-of-commits\":" + numberOfCommits + "}");
                    refsJson.close();
                } catch (Exception e) {
                    System.out.println("lvn: failed to create refs.json file.");
                }
                
                //run versioning to files already versioned again
                for (int i = 0; i < refsJsonObjectsArray.length(); i++) {
                    JSONObject refFile = refsJsonObjectsArray.getJSONObject(i);
                    
                    this.addFileToVersioning(refFile.getString("path"));
                }
            }
        } catch (Exception e) {
            System.out.println("lvn: " + e);
        }
    }
}