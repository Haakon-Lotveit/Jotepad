package no.haakon.jotepad.old.actions;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

final public class SaveAsAction extends AbstractSaveAction {

    public static final String COMMAND_ROOT = "LAGRE_SOM";

    public SaveAsAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectNewFile();
    }
}
