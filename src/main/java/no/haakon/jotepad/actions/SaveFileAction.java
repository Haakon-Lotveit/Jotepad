package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;

final public class SaveFileAction extends AbstractSaveAction {

    public static final String COMMAND_ROOT = "LAGRE";

    public SaveFileAction(Editor editor) {
        super(COMMAND_ROOT, editor);
    }
    public SaveFileAction(Editor editor, KeyStroke shortcut) {
        super(COMMAND_ROOT, editor, shortcut);
    }

    /**
     * Denne metoden er egentlig veldig enkel, men kan virke litt sær om du ikke er vant til Optional:
     * Dersom det finnes en fil assosiert med editoren, blir denne valgt til å lagre med.
     * Men dersom det ikke finnes en, blir selectNewFile kjørt, og den vil i så fall lagre.
     * @param actionEvent blir ignorert.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        editor.getFile().ifPresentOrElse(this::save, this::selectNewFile);
    }
}
