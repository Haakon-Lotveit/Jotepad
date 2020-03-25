package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Optional;

public class FindTextAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SØK";

    public FindTextAction(Editor editor, KeyStroke shortcut) {
        super(COMMAND_ROOT, editor, Collections.singleton(shortcut));
    }

    /**
     * Denne søkefunksjonen har sine begrensinger for å si det mildt.
     * Den kan kun søke framover, og det er ikke bra nok i lengden.
     * @param e ignorert
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        editor.inputBox("Finn", "Søk etter:").ifPresent(this::søk);
    }


    private void søk(final String søkEtter) {
        final int posisjon = editor.getCaretPosition();
        final String tekst = editor.getText();
        final int fra = tekst.indexOf(søkEtter, posisjon);
        if(fra < 0) {
            editor.popupInfo("Finner ikke", "Kan ikke finne teksten din");
            editor.setCaretPosition(posisjon);
            return;
        }
        final int til = fra + søkEtter.length();

        editor.setSelectionStart(fra);
        editor.setSelectionEnd(til);
    }
}
