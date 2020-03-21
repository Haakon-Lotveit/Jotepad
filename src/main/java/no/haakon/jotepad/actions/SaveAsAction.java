package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.TextEditorPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

final public class SaveAsAction extends AbstractSaveAction {

    public static final String COMMAND_ROOT = "LAGRE_SOM";

    public SaveAsAction(TextEditorPane editor, KeyStroke shortcut) {
        super(COMMAND_ROOT, editor, shortcut);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectNewFile();
    }
}
