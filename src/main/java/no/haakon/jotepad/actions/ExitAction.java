package no.haakon.jotepad.actions;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitAction extends AbstractJotepadAction {

    public static final String COMMAND_ROOT = "EXIT";

    public ExitAction(Editor editor, KeyStroke shortcut) {
        super(COMMAND_ROOT, editor, shortcut);
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
