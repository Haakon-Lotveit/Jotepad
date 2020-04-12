package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class NewFileAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "NY-FIL";

    public NewFileAction(Editor editor) {
        super(COMMAND_ROOT, editor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.newFile();
    }
}
