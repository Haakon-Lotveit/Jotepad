package no.haakon.jotepad.actions.tabell;

import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.model.buffer.Buffer;
import no.haakon.jotepad.model.buffer.tabell.TabellBuffer;

import java.awt.event.ActionEvent;

public class NyRadAction extends AbstractJotepadAction {
    public static String COMMAND_ROOT = "NY_RAD";

    public NyRadAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Buffer valgtBuffer = frame.synligBuffer();
        if(!(valgtBuffer instanceof TabellBuffer)) {
            System.err.println("Nåværende buffer er ikke en tabellbuffer, kan ikke sette inn ny rad.");
            return;
        }

        TabellBuffer buffer = (TabellBuffer) valgtBuffer;

        int valgtRad = Integer.max(0, buffer.getComponent().getSelectedRow());

        buffer.getModell().settInnRad(valgtRad);

        buffer.getComponent().setRowSelectionInterval(valgtRad, valgtRad);
    }
}
