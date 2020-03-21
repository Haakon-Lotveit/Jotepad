package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.TextEditorPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

public class NewFileAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "NY-FIL";

    public NewFileAction(TextEditorPane editor, KeyStroke... shortcuts) {
        super(COMMAND_ROOT, editor, shortcuts);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.newFile();
    }
}
