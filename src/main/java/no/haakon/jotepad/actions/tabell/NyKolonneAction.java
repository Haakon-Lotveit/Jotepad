package no.haakon.jotepad.actions.tabell;

import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.model.buffer.tabell.TabellBuffer;

import java.awt.event.ActionEvent;

public class NyKolonneAction extends AbstractJotepadAction {

    public static String COMMAND_ROOT = "NY_KOLONNE";

    public NyKolonneAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Bedt om ny kolonne, ikke implementert enda...");
        if(!(frame.synligBuffer() instanceof TabellBuffer)) {
            System.err.println("Synlig buffer er ikke en tabellbuffer, så tabelloperasjoner kan ikke utføres");
            return;
        }
        TabellBuffer buffer = (TabellBuffer) frame.synligBuffer();
        int valgtKolonne = Integer.max(0, buffer.getComponent().getSelectedColumn());

        String kolonnenavn = buffer.guiHelpers().inputBox("Ny kolonne...", "Hva skal den nye kolonnen hete?").orElse("");

        System.out.printf("Ny kolonne %d, med navn '%s'%n", valgtKolonne, kolonnenavn);

        buffer.getModell().settInnKolonne(kolonnenavn, valgtKolonne);
    }
}
