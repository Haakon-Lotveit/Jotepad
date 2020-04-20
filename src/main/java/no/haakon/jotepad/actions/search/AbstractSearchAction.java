package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.ApplicationFrame;

import java.util.UUID;

public abstract class AbstractSearchAction extends AbstractJotepadAction {

    protected static final String SEARCH_STATE_PREFIX = "SØK-" + UUID.randomUUID();
    protected static final String SEARCH_TERM = "TERM";
    protected static final String SEARCH_TYPE = "TYPE";


    public AbstractSearchAction(String commandRoot, ApplicationFrame editor) {
        super(commandRoot, editor);
        frame.setValue(SEARCH_STATE_PREFIX + SEARCH_TYPE, Søketype.INGEN.name());
    }


    protected void setSimpleSearchTerm(String searchTerm) {
        if(searchTerm == null || searchTerm.isEmpty()) {
            System.err.println("Prøvde å sette søketerm til en tom verdi, ingenting blir satt.");
            return;
        }
        frame.setValue(SEARCH_STATE_PREFIX + SEARCH_TERM, searchTerm);
        frame.setValue(SEARCH_STATE_PREFIX + SEARCH_TYPE, Søketype.ENKELT.name());
    }

    protected String getSearchTerm() {
        return frame.getValue(SEARCH_STATE_PREFIX + SEARCH_TERM);
    }

    protected Søketype getSøketype() {
        return Søketype.valueOf(frame.getValue(SEARCH_STATE_PREFIX + SEARCH_TYPE));
    }
}
