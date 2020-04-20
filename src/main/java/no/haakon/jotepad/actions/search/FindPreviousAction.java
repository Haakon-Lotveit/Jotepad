package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class FindPreviousAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SØK-BAK";

    public FindPreviousAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getSøketype().søker(frame.synligBuffer()).searchBackwards(getSearchTerm());
    }
}
