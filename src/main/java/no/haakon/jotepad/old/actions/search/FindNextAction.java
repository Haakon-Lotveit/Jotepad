package no.haakon.jotepad.old.actions.search;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.model.buffer.tekst.AbstractTekstBuffer;

import java.awt.event.ActionEvent;

public class FindNextAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SØK-FRAM";

    public FindNextAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(frame.synligBuffer() instanceof AbstractTekstBuffer) {
            getSøketype().søker((AbstractTekstBuffer) frame.synligBuffer()).searchForward(getSearchTerm());
        }
    }


}
