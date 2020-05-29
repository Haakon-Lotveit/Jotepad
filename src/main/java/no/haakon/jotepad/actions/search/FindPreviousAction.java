package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.model.buffer.tekst.AbstractTekstBuffer;

import java.awt.event.ActionEvent;

public class FindPreviousAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SØK-BAK";

    public FindPreviousAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(frame.synligBuffer() instanceof AbstractTekstBuffer) {
            getSøketype().søker((AbstractTekstBuffer)frame.synligBuffer()).searchBackwards(getSearchTerm());
        }
    }
}
