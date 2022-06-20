package priv.welber.ds.assigenment1.server;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class ServerLauncher {
    public static void main(String[] args) {
        try {
            if (args.length != 2){
                System.out.println("Please enter dictionary file path and port in order!");
            } else {
                new DictionaryServer(args[0], args[1]);
            }
        } catch (Exception e){
            System.out.println("Server launcher error!");
        }

    }


}
