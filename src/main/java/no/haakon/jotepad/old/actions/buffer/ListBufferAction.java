package no.haakon.jotepad.old.actions.buffer;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.gui.components.inputlister.BufferFinnBufferVindu;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ListBufferAction extends AbstractBufferAction {
    public static final String COMMAND_ROOT = "BUFFER_LIST";

    public ListBufferAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        SwingUtilities.invokeLater(() -> new BufferFinnBufferVindu(frame.getBuffere(), frame));
    }
}
