package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.ApplicationFrame;

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
        frame.synligBuffer().inputBox("Finn", "Søk etter:").ifPresent(this::søk);
    }


    private void søk(final String søkEtter) {
        final String tekst = frame.synligBuffer().getText();
        final int fra = tekst.indexOf(søkEtter);
        final int til = fra + tekst.length();
        if(fra < 0) {
            frame.synligBuffer().popupInfo("Finner ikke", "Kan ikke finne teksten din");
            return;
        }

        frame.synligBuffer().setSelectionStart(fra);
        frame.synligBuffer().setSelectionEnd(til);
    }
}
