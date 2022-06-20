package priv.welber.ds.assigenment1.client;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class ClientLauncher {
    public static void main(String[] args) {
        try {
            if (args.length != 2){
                System.out.println("Please enter host and port in order!");
            } else {
                new DictionaryClient(args[0], args[1]);
            }
        } catch (Exception e){
            System.out.println("Client launcher error!");
        }
    }
}
