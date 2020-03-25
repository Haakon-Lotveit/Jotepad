package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

public class FindPreviousAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SØK-BAK";

    public FindPreviousAction(Editor editor, KeyStroke shortcut) {
        super(COMMAND_ROOT, editor, shortcut);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getSøketype().søker(editor).searchBackwards(getSearchTerm());
    }
}
