package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ShowSearchTermAction extends AbstractSearchAction {

    public static String COMMAND_ROOT = "VIS-SØKETERM";

    public ShowSearchTermAction(Editor editor) {
        super(COMMAND_ROOT, editor, Collections.emptyList());
    }

    public ShowSearchTermAction(Editor editor, KeyStroke[] shortcuts) {
        super(COMMAND_ROOT, editor, Arrays.asList(shortcuts));
    }

    public ShowSearchTermAction(Editor editor, Collection<KeyStroke> shortcuts) {
        super(COMMAND_ROOT, editor, shortcuts);
    }

    public ShowSearchTermAction(Editor editor, KeyStroke shortcut) {
        super(COMMAND_ROOT, editor, Collections.singleton(shortcut));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String searchTerm = getSearchTerm();
        if(searchTerm == null) {
            editor.popupInfo("Søketerm", "Ingen søketerm har blitt satt");
            return;
        }

        editor.popupInfo("Søketerm", "Søketerm: " + searchTerm);
    }
}
