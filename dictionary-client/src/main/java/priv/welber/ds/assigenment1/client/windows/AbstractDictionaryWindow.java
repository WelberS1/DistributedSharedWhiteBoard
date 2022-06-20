package priv.welber.ds.assigenment1.client.windows;

import javax.swing.*;
import java.awt.*;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public abstract class AbstractDictionaryWindow {
    public void updateTitle(Frame frm, boolean connectionStatus, String title){
        if (connectionStatus){
            frm.setTitle(title);
        } else {
            frm.setTitle(title + "(Connecting...)");
        }
    }

    public abstract JFrame getFrame();
    public abstract String getTitle();
}