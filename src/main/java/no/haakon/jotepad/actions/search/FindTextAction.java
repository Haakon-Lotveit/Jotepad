package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.model.buffer.tekst.AbstractTekstBuffer;
import no.haakon.jotepad.model.buffer.Buffer;

import java.awt.event.ActionEvent;

public class FindTextAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SØK";

    public FindTextAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    /**
     * Denne søkefunksjonen har sine begrensinger for å si det mildt.
     * Den kan kun søke framover, og det er ikke bra nok i lengden.
     * @param e ignorert
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.synligBuffer().guiHelpers().inputBox("Finn", "Søk etter:").ifPresent(this::søk);
    }


    private void søk(final String søkEtter) {
        AbstractTekstBuffer buffer;
        {
            Buffer bufferen = frame.synligBuffer();
            if(bufferen instanceof AbstractTekstBuffer) {
                buffer = (AbstractTekstBuffer) bufferen;
            } else {
                System.err.println("Ikke en tekstbuffer, kan ikke søke.");
                return;
            }
        }

        final String tekst = buffer.getEditor().getTekst();
        final int fra = tekst.indexOf(søkEtter);
        final int til = fra + søkEtter.length();
        if(fra < 0) {
            frame.synligBuffer().guiHelpers().popupInfo("Finner ikke", "Kan ikke finne teksten din");
            return;
        }

        buffer.getComponent().setSelectionStart(fra);
        buffer.getComponent().setSelectionEnd(til);
    }
}
