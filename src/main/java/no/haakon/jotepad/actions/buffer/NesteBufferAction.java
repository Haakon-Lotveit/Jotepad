package no.haakon.jotepad.actions.buffer;

import no.haakon.jotepad.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class NesteBufferAction extends AbstractBufferAction {

    public static final String COMMAND_ROOT = "BUFFER_NESTE";

    public NesteBufferAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        frame.visNesteBuffer();
    }
}
