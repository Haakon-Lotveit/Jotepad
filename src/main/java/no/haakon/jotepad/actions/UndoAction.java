package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class UndoAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "ANGRE";

    public UndoAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent ignored) {
        frame.synligBuffer().getUndoer().undo();
    }
}
