package no.haakon.jotepad.old.actions;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class ExitAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "EXIT";

    public ExitAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    /**
     * This is obviously not the correct behaviour, but for now it's good enough:
     * If you click exit, it will exit, not saving, asking or anything of the sort.
     * But that's kind of okay for now, because we have no way of doing those things regardless.
     * @param e is ignored completely.
     */
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
