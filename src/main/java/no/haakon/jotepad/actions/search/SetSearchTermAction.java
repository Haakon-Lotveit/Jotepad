package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Vanlig søk er ment til å være ganske enkel:
 * Du har tre kommandoer: En for å si hva du vil søke etter, en for å finne neste (etter markøren) og en til å finne forrige (før markøren).
 * Søking vil flytte markøren. Dette er handlingen for å sette søketermen.
 */
public class SetSearchTermAction extends AbstractSearchAction {

    public static final String COMMAND_ROOT = "SETT-SØKETERM";

    public SetSearchTermAction(Editor editor) {
        super(COMMAND_ROOT, editor, Collections.emptyList());
    }

    public SetSearchTermAction(Editor editor, KeyStroke[] shortcuts) {
        super(COMMAND_ROOT, editor, Arrays.asList(shortcuts));
    }

    public SetSearchTermAction(Editor editor, Collection<KeyStroke> shortcuts) {
        super(COMMAND_ROOT, editor, shortcuts);
    }

    public SetSearchTermAction(Editor editor, KeyStroke shortcut) {
        super(COMMAND_ROOT, editor, Collections.singleton(shortcut));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.inputBox("Søketerm", "Hva vil du søke etter? ").ifPresent(this::setSimpleSearchTerm);
    }
}
