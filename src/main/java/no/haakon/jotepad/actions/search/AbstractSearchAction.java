package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public abstract class AbstractSearchAction extends AbstractJotepadAction {

    protected static final String SEARCH_STATE_PREFIX = "SØK-" + UUID.randomUUID();
    protected static final String SEARCH_TERM = "-TERM";
    protected static final String SEARCH_TYPE = "-TYPE";


    public AbstractSearchAction(String commandRoot, Editor editor, Collection<KeyStroke> shortcuts) {
        super(commandRoot, editor, shortcuts);
        editor.setValue(SEARCH_STATE_PREFIX + SEARCH_TYPE, Søketype.INGEN.name());
    }

    public AbstractSearchAction(String commandRoot, Editor editor, KeyStroke shortcut) {
        this(commandRoot, editor, Collections.singleton(shortcut));
    }

    protected void setSimpleSearchTerm(String searchTerm) {
        if(searchTerm == null || searchTerm.isEmpty()) {
            System.err.println("Prøvde å sette søketerm til en tom verdi, ingenting blir satt.");
            return;
        }
        editor.setValue(SEARCH_STATE_PREFIX + SEARCH_TERM, searchTerm);
        editor.setValue(SEARCH_STATE_PREFIX + SEARCH_TYPE, Søketype.ENKELT.name());
    }

    protected String getSearchTerm() {
        return editor.getValue(SEARCH_STATE_PREFIX + SEARCH_TERM);
    }

    protected Søketype getSøketype() {
        return Søketype.valueOf(editor.getValue(SEARCH_STATE_PREFIX + SEARCH_TYPE));
    }
}
