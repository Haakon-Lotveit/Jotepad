package no.haakon.jotepad.old.actions;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class UndoAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "ANGRE";

    public UndoAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent ignored) {
        frame.synligBuffer().getEditor().undo();
    }
}
