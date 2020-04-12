package no.haakon.jotepad.gui.components;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

/**
 * Wrapper resultatet fra JFileChooser, så vi ender opp med enumer istedenfor int-verdier.
 */
public enum FileChooserOption {

    APPROVE, CANCEL, ERROR;


    public static FileChooserOption from(int jFileChooserReturnValue) {
        switch (jFileChooserReturnValue) {
            case JFileChooser.APPROVE_OPTION:
                return APPROVE;
            case JFileChooser.CANCEL_OPTION:
                return CANCEL;
            case JFileChooser.ERROR_OPTION:
                return ERROR;
            default:
                throw new IllegalArgumentException("Tallet " + jFileChooserReturnValue + " representerer ikke en gyldig verdi for JFileChooser");
        }
    }
}
