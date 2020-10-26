package no.haakon.jotepad.old.actions;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class NewFileAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "NY-FIL";

    public NewFileAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.nyTomBuffer();
    }
}
