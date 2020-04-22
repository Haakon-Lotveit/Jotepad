package no.haakon.jotepad.actions.buffer;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.BufferFinnBufferVindu;
import no.haakon.jotepad.model.buffer.Buffer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

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
