package br.uff.ic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.json.JSONObject;

public class CommitInfoTest {
    
    @Test
    public void shouldReturnLineJsonObject() {

        CommitInfo commitInfo = new CommitInfo();
        
        String hash = "d0660d69d8f46a448e8603e91ffa5d279276ca9b";
        String author = "mathunes <matheusantunes720@gmail.com>";
        String date = "Mon Nov 7 20:01:55 2022 -0300";
        String message = "add version of line deleted";
        String content = "package br.uff.ic;";

        commitInfo.setHash(hash);
        commitInfo.setAuthor(author);
        commitInfo.setDate(date);
        commitInfo.setMessage(message);

        JSONObject lineJsonObject = commitInfo.getLineObject(content);

        assertEquals(lineJsonObject.getString("hash"), hash);
        assertEquals(lineJsonObject.getString("author"), author);
        assertEquals(lineJsonObject.getString("date"), date);
        assertEquals(lineJsonObject.getString("message"), message);
        assertEquals(lineJsonObject.getString("content"), content);

    }

}
