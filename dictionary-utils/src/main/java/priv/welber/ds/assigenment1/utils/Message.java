package priv.welber.ds.assigenment1.utils;

import java.io.Serializable;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class Message implements Serializable {
    private Commands command;
    private String word;
    private String meaning;
    private String response;
    
    public Message() {
    }

    public Commands getCommand() {
        return command;
    }
    public void setCommand(Commands command) {
        this.command = command;
    }

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
}