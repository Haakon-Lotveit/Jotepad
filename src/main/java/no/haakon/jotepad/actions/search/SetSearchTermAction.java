package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

/**
 * Vanlig søk er ment til å være ganske enkel:
 * Du har tre kommandoer: En for å si hva du vil søke etter, en for å finne neste (etter markøren) og en til å finne forrige (før markøren).
 * Søking vil flytte markøren. Dette er handlingen for å sette søketermen.
 */
public class SetSearchTermAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SETT-SØKETERM";

    public SetSearchTermAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.synligBuffer().inputBox("Søketerm", "Hva vil du søke etter? ").ifPresent(this::setSimpleSearchTerm);
    }
}
