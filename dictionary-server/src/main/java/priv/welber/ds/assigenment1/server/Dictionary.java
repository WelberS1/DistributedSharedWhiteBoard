package priv.welber.ds.assigenment1.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import priv.welber.ds.assigenment1.utils.*;

import java.io.File;
import java.util.HashMap;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class Dictionary {

    private HashMap<String, String> dictionary = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();
    private File file;
    private String dictionaryFilePath;

    public Dictionary(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
        initialize();
    }

    public void initialize(){
        this.loadFile(dictionaryFilePath);
        this.fileToDictionary();
    }

    public boolean loadFile(String dictionaryFilePath){
        boolean successFlag = false;
        file = new File(dictionaryFilePath);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (Exception e) {
                System.out.println("Dictionary file load error!");
                System.exit(0);
            }
        }
        return successFlag;
    }

    public void fileToDictionary(){
        try {
            this.dictionary = mapper.readValue(this.file, HashMap.class);
        } catch (Exception e) {
            System.out.println("Cannot load dictionary data!");
            System.out.println("New dictionary data has been created!");
            System.out.println("The original file has been overwritten!");
        }
    }

    public synchronized boolean writeFile(){
        try {
            mapper.writeValue(this.file, dictionary);
            return true;
        } catch (Exception e) {
            System.out.println("File write error!");
            return false;
        }
    }

    public synchronized String operation(Message message){
        Commands command = message.getCommand();
        String word = message.getWord();
        String meaning = message.getMeaning();

        if (command == Commands.QUERY || command == Commands.REMOVE || command == Commands.UPDATE) {
            if (dictionary.get(word) == null){
                return "Not Found!";
            }
            switch (command){
                case QUERY: {
                    return dictionary.get(word);
                }
                case REMOVE: {
                    dictionary.remove(word);
                    return "Success!";
                }
                case UPDATE: {
                    dictionary.put(word, meaning);
                    return "Success!";
                }
            }
        } else if (command == Commands.ADD){
            if (dictionary.get(word) != null){
                return "Duplicate!";
            } else {
                dictionary.put(word, meaning);
                return "Success!";
            }
        }
        return "Invalid command!";
    }

}