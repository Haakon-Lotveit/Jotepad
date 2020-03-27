package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class FindNextAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SØK-FRAM";

    public FindNextAction(Editor editor) {
        super(COMMAND_ROOT, editor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getSøketype().søker(editor).searchForward(getSearchTerm());
    }


}
