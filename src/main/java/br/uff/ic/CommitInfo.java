package br.uff.ic;

import org.json.JSONObject;

public class CommitInfo {
    
    private String hash;
    private String author;
    private String date;
    private String message;

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getLineObject(String contentLine) {
        String content = "\"content\": \""+ contentLine.replaceAll("\"", "\\\\\"").replaceAll("\n", "") +"\",";
        String author = "\"author\": \""+ this.author.replaceAll("\n", "") +"\",";
        String date = "\"date\": \""+ this.date.replaceAll("\n", "") +"\",";
        String message = "\"message\": \""+ this.message.replaceAll("\n", "") +"\",";
        String hash = "\"hash\": \""+ this.hash.replaceAll("\n", "") +"\"";

        return new JSONObject("{" + content + author + date + message + hash + "}");
    }
}