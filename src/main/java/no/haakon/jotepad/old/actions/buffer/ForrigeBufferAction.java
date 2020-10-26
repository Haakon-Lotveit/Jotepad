package no.haakon.jotepad.old.actions.buffer;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class ForrigeBufferAction extends AbstractBufferAction {

    public static final String COMMAND_ROOT = "BUFFER_FORRIGE";

    public ForrigeBufferAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.visForrigeBuffer();
    }
}
