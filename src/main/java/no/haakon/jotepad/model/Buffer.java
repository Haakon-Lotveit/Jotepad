package no.haakon.jotepad.model;

import javax.swing.*;
import java.io.File;

public interface Buffer {
    public File getFil();

    /**
     * @return navnet på denne typen buffer (til dømes: "Tekst")
     */
    public String getTypeNavn();

    /**
     * @return navnet på denne spesifikke bufferen. (til dømes "Tekst: C:\Users\bruker\Documents\notat.txt")
     */
    public String getBufferNavn();

    default void die() {
        getComponent().setEnabled(false);
    }

    public JComponent getComponent();
}
