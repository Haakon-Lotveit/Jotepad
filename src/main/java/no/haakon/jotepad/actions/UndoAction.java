package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "ANGRE";

    public UndoAction(Editor editor) {
        super(COMMAND_ROOT, editor);
    }

    @Override
    public void actionPerformed(ActionEvent ignored) {
        editor.getUndoer().undo();
    }
}
