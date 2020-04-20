package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.ApplicationFrame;

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
